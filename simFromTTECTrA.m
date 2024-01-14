function outputs=simFromTTECTrA(inputs)
%       simFromTTECTrA.m
%**********************************************************************
% Written by Alicia Zinnecker (N&R Engineering)
% NASA Glenn Research Center, Cleveland, OH
% April 2nd, 2013
%
%   This file acts as an interface between the tool for turbine engine
%   closed-loop transient analysis (TTECTrA) and the engine model.  In
%   particular, given inputs from TTECTrA, this file performs model-
%   specific tasks to set up the workspace for simulation, runs the
%   simulation, then returns the results.
%
%   The file is organized so as to separate model-specific tasks from those
%   tasks related setting up the TTECTrA control block.
%
%   Simulation results are returned in outputs, a structure that must
%   contain the following fields for control design:
%     t -- simulation time instants
%     Fnet -- uncorrected thrust
%     Wf_vec -- fuel flow
%     P2 -- pressure at station 2 (use to correct Fnet)
%     T25 -- temperature at station 25 (use to correct Nc)
%     Nc -- core speed
%     NcR25 -- core speed corrected to station 25
%     HPC_SM -- surge margin of HPC
%     LPC_SM -- surge margin of LPC
%   The output variable also contains the following fields, from the
%   controller block:
%     CV_fdbk -- feedback (control) variable
%     CV_dmd -- control variable demand
%     FT_dmd -- corrected thrust demand
%     Wf_dmd -- fuel flow demand
%   In addition, other fields may be specified by the user for inclusion in
%   the output structure (e.g. Ps3).
%--------------------------------------------------------------------------
%  REQUIREMENTS:   
%       Maltab(R) control systems toolbox
% *************************************************************************

%----------------------------------------------------
% Model-specific workspace setup:
%  - User modifies for their model
%  -> Set up model at defined flight condition (input.in.*)
%  -> Trim the engine (if desired)
%  -> Set other defaults
%----------------------------------------------------

% setup vectors defining altitude, Mach number, dTamb (and time)
% organization of these inputs is model-specific (e.g. each variable may be
% separate, or part of a structure)
in.t_vec = inputs.in.t_vec;
in.alt   = inputs.in.alt;
in.MN    = inputs.in.MN;
in.dTamb = inputs.in.dTamb;

% specify default model to simulate if it is not defined in input argument
if ~isfield(inputs,'in') || ~isfield(inputs.in,'simFileName') || isempty(inputs.in.simFileName)
    inputs.in.simFileName = 'TTECTrA_example.slx'; % filename and extension
end

addpath(genpath('example_model'));

% trim the model to the initial thrust demand, if specified
% (this is necessary if the model setup process uses a different variable,
% such as fuel flow or throttle command, to find the initial conditions)
if isfield(inputs.in,'FT_dmd');
    wf_0 = trim_model(inputs.in.FT_dmd(1),9);
else
    wf_0 = inputs.in.wf_vec(1);
end

% run any setup code necessary to add paths, create workspace variables to
% enable the simulation to run sucessfully
MWS=setup_workspace(in.t_vec,wf_0);

% Model specific data required for simulation (RHS specified by model)
DWS.in.Ts_cont = MWS.Ts;         % model sampling time
DWS.in.Wf_zro  = MWS.IC.Wf_0;    % initial fuel flow
DWS.in.Nc_zro  = MWS.IC.Nc_0;    % initial core speed
DWS.in.P2      = MWS.IC.P2_0;    % initial P2, used for correction
DWS.in.T2      = MWS.IC.T2_0;    % initial T2, used for correction

%----------------------------------------------------
% Create workspace variable for TTECTrA controller
%   - No user modification necessary
%----------------------------------------------------
DWS=setup_TTECTrA_block(DWS,inputs);

%----------------------------------------------------
% Model execution setup:
%   - User defines variables for the specified outputs as they are named in
%     the model
%----------------------------------------------------
try
    y=sim(inputs.in.simFileName,'SrcWorkspace','current','ReturnWorkspaceOutputs','on','StopTime',num2str(inputs.in.t_vec(end)));

    % organize simulation results in structure outputs

    % ----
    % REQUIRED OUTPUT VARIABLES
    % ----
    % for controller design
    %  > variables names are model-specific
    outputs.t       = y.get('tout');        
    outputs.P2      = y.get('P2');
    outputs.Fnet    = y.get('Fnet')./(outputs.P2/14.696);
    outputs.Wf_vec  = y.get('Wf');
    outputs.T25     = y.get('T25');
    outputs.Nc      = y.get('Nc');
    outputs.NcR25   = y.get('NcR');
    outputs.Nc_dot  = y.get('Nc_dot');
    outputs.HPC_SM  = y.get('SM_HPC');
    outputs.LPC_SM  = y.get('SM_LPC');

    % from controller block
    %  > Control variable feedback
    %  > Demand signals for control variables
    outputs.CV_fdbk = y.get('out_Fdbk');
    outputs.CV_dmd  = y.get('out_dmd');
    outputs.FT_dmd  = y.get('out_Fnet_dmd');
    outputs.Wf_dmd  = y.get('out_Wf_dmd');

    % ----
    % ADDITIONAL (OPTIONAL) OUTPUT VARIABLES
    % ----
    outputs.Ps3     = y.get('Ps3');

catch ME  % if any errors are encountered during simulation, empty outputs are assigned
    errordlg({'Error running simulation:',ME.message})
    outputs=[];
end