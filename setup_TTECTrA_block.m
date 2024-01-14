function DWS=setup_TTECTrA_block(DWS,inputs)
%       setup_TTECTrA_block.m
%*************************************************************************
% Written by Alicia Zinnecker (N&R Engineering) and Jeffrey Csank (NASA)
% NASA Glenn Research Center, Cleveland, OH
% May 2013
%
% This file contains the code to setup the workspace variable for the tool
% for turbine engine closed-loop transient analysis (TTECTrA) block in
% order for the simulation to run.
%*************************************************************************

%----------------------------------------------------
% TTECTrA Simulink Block Setup:
%  - Creates the workspace variable DWS used exclusively by the 
%    controller block to define various parameters
%----------------------------------------------------
% Simulation parameters
DWS.in.t_vec = inputs.in.t_vec;    % time vector for input command
DWS.in.loop  = inputs.in.loop;     % indicates which controller to use

% Assign input command depending on loop value
if DWS.in.loop == 3     % fuel flow profile => default thrust profile
    if length(inputs.in.wf_vec) == 1
        DWS.in.wf_vec=inputs.in.wf_vec*ones(1,length(inputs.in.t_vec));
    else
        DWS.in.wf_vec=inputs.in.wf_vec;
    end
    DWS.in.FT_dmd=30000*ones(1,length(inputs.in.t_vec));
else                    % thrust profile => default fuel flow profile
    if length(inputs.in.FT_dmd) == 1
        DWS.in.FT_dmd=inputs.in.FT_dmd*ones(1,length(inputs.in.t_vec));
    else
        DWS.in.FT_dmd=inputs.in.FT_dmd;
    end
    DWS.in.wf_vec=0.5*ones(1,length(inputs.in.t_vec));
end

% Setup the controller: define default values if not provided

% Controller feedback filter
if isfield(inputs,'controller') && ~isempty(inputs.controller.FdbkFilterBW) && inputs.controller.FdbkFilterBW>0
    DWS.in.Fdbk_flag=1;
    DWS.TTECTrA_controller.Fdbk_num_Z = [1-exp(-inputs.controller.FdbkFilterBW*DWS.in.Ts_cont)];
    DWS.TTECTrA_controller.Fdbk_den_Z = [1 -exp(-inputs.controller.FdbkFilterBW*DWS.in.Ts_cont)];
else
    DWS.in.Fdbk_flag=0;
    DWS.TTECTrA_controller.Fdbk_num_Z = [1-exp(-10.0*DWS.in.Ts_cont)];
    DWS.TTECTrA_controller.Fdbk_den_Z = [1 -exp(-10.0*DWS.in.Ts_cont)];
end

% Prefiler on thrust demand
if isfield(inputs,'controller') && isfield(inputs.controller,'PreFilterBW') && ~isempty(inputs.controller.PreFilterBW)
    DWS.TTECTrA_controller.PreFilterBW = inputs.controller.PreFilterBW;
else
    DWS.TTECTrA_controller.PreFilterBW = 2;
end

% Controller gains
if isfield(inputs,'gains') && sum(isfield(inputs.gains,{'Fdbk','Kp','Ki'}))==3 && ~isempty(inputs.gains.Fdbk) && ~isempty(inputs.gains.Kp) && ~isempty(inputs.gains.Ki) % P, I gains have been calculated
    I_corr = (DWS.in.P2 / 14.696)^2; % theta squared
    
    DWS.TTECTrA_controller.P_gain=inputs.gains.Kp/I_corr;
    DWS.TTECTrA_controller.I_gain=inputs.gains.Ki/I_corr;
    DWS.TTECTrA_controller.Fdbk=inputs.gains.Fdbk;
    
    if isfield(inputs.controller,'IWUP_gain')
        DWS.TTECTrA_controller.IWP = inputs.controller.IWUP_gain;
    elseif isfield(inputs.SP,'SP')
        DWS.TTECTrA_controller.IWP = max(inputs.SP.SP)/10;
    else
        DWS.TTECTrA_controller.IWP=max(inputs.gains.Fdbk)/100;
    end
    
else    % no gains have been provided
    if DWS.in.loop == 1     % warn if CV loop is indicated
        display('WARNING -- No controller gains specified, using default values');
    end
    DWS.TTECTrA_controller.P_gain=[1 2];
    DWS.TTECTrA_controller.I_gain=[1 2];
    DWS.TTECTrA_controller.Fdbk=[1000 5000];
    DWS.TTECTrA_controller.IWP=3000;
end

% Fuel actuator dynamics
if isfield(inputs,'actuator') && isfield(inputs.actuator,'wf_bw')
    DWS.TTECTrA_Wf.bandwidth=inputs.actuator.wf_bw;
else
    if DWS.in.loop == 1     % warn if CV loop is indicated
        display('WARNING -- No fuel actuator bandwidth specified, using default value');
    end
    DWS.TTECTrA_Wf.bandwidth=10;
end

% Control variable setpoints
if isfield(inputs,'SP') && sum(isfield(inputs.SP,{'FT_bkpt','SP'}))==2 && ~isempty(inputs.SP.FT_bkpt) && ~isempty(inputs.SP.SP)  % setpoints have been calculated
    DWS.TTECTrA_setpoints.FT_bkpt=inputs.SP.FT_bkpt;
    DWS.TTECTrA_setpoints.SP=inputs.SP.SP;
else    % no setpoints have been provided, use defaults
    if DWS.in.loop == 1
        display('WARNING -- No setpoints have been calculated, using default values');
    end
    
    DWS.TTECTrA_setpoints.FT_bkpt=[1 2];
    DWS.TTECTrA_setpoints.SP=[1 2];
end

% Accel and decel limiters
if isfield(inputs,'Limiter')    % at least one limiter has been calculated
    if isfield(inputs.Limiter,'HPC_Limiter') && sum(isfield(inputs.Limiter.HPC_Limiter,{'NcR25_sched','Ncdot_sched'}))==2 && ~isempty(inputs.Limiter.HPC_Limiter.NcR25_sched) && ~isempty(inputs.Limiter.HPC_Limiter.Ncdot_sched)
        DWS.TTECTrA_limiter.Nc_sched=inputs.Limiter.HPC_Limiter.NcR25_sched;
        DWS.TTECTrA_limiter.Ncdot_sched=inputs.Limiter.HPC_Limiter.Ncdot_sched*0.9;
    else    % no accel schedule has been provided, use default
        if DWS.in.loop == 1
            display('WARNING -- No acceleration schedule has been calculated, using default schedule');
        end
        
        DWS.TTECTrA_limiter.Nc_sched=[7500 11000];
        DWS.TTECTrA_limiter.Ncdot_sched=[150 150];
    end
    
    if isfield(inputs.Limiter,'LPC_Limiter') && ~isempty(inputs.Limiter.LPC_Limiter)
        DWS.TTECTrA_limiter.WfPs3lim = inputs.Limiter.LPC_Limiter;
    else    % no decel limiter has been provided, use default
        if DWS.in.loop == 1
            display('WARNING -- No deceleration limiter has been calculated, using default values');
        end
        
        DWS.TTECTrA_limiter.WfPs3lim = 1;
    end
else        % no limiters have been provided, use default
    if DWS.in.loop == 1
        display('WARNING -- No limiters have been calculated, using default values');
    end
    
    DWS.TTECTrA_limiter.Nc_sched=[7500 11000];
    DWS.TTECTrA_limiter.Ncdot_sched=[150 150];

    DWS.TTECTrA_limiter.WfPs3lim = 1;    
end

% Gains for accel schedule limiter
limiter_gain=6.0159e-4;  % dc gain
limiter_bw=3.0602;       % controller bandwidth
bw_factor=interp1([0:5000:40000],[0.9923 0.8336 0.6918 0.5667 0.4584 0.3669 0.2923 0.2343 0.1932], inputs.in.alt);
DWS.TTECTrA_limiter.Kp_accel=limiter_gain;
DWS.TTECTrA_limiter.Ki_accel=limiter_gain*limiter_bw/bw_factor;
DWS.TTECTrA_limiter.IWP_accel=5000;

DWS.TTECTrA_limiter.accel_num = [1-exp(-1.5*DWS.in.Ts_cont)];
DWS.TTECTrA_limiter.accel_den = [1 -exp(-1.5*DWS.in.Ts_cont)];