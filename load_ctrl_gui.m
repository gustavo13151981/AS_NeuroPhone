function [output,check]=load_ctrl_gui
%       load_ctrl_gui.m
%********************************************************************
% Written by Alicia Zinnecker (N&R Engineering) and Jeffrey Csank (NASA)
% NASA Glenn Research Center, Cleveland, OH
% May 2013
%
% This file prompts the user to load a pre-designed controller for use by
% the generic engine control module.
%********************************************************************
close all;

%--------------------------------
% Configure the GUI window
%--------------------------------
S.fh = figure('units','normalized',...
    'position',[.1 .2 .2 .2],...
    'menubar','none',...
    'name','TTECTrA: Load Controller Data',...
    'numbertitle','off',...
    'visible','off',...
    'resize','on');

S.panel1 = uipanel('units','normalized',...
    'pos',[.01 .01 .98 .98],...
    'fontsize',12,...
    'TitlePosition', 'centertop');

%------ File Name Panel ------
S.lmtx(1) = uicontrol(S.panel1,...
    'style','text',...
    'unit','normalized',...
    'position',[.05 .75 .25 .15],...
    'HorizontalAlignment','center',...
    'fontsize',9, ...
    'String','File name');

S.FN_box = uicontrol(S.panel1,...
    'style','edit',...
    'unit','normalized',...
    'position',[0.40 0.75 0.5 0.17],...
    'HorizontalAlignment','left',...
    'fontsize',10,...
    'backgroundcolor','white');

S.FNbtn = uicontrol(S.panel1,...
    'style','push',...
    'unit','normalized',...
    'position',[.10 .40 .80 .25],...
    'fontsize',10,...
    'string','Choose File to Load',...
    'enable', 'on',...
    'callback',{@get_file_call,S});

S.LDbtn = uicontrol(S.panel1,...
    'style','push',...
    'unit','normalized',...
    'position',[.10 .10 .80 .25],...
    'fontsize',10,...
    'string','Load Data',...
    'enable', 'on',...
    'callback',{@load_call,S});

%--- move and show gui
movegui(S.fh,'center');
set(S.fh,'visible','on'); % Make the GUI visible.
set(S.fh,'closerequestfcn',@close_gui_call)

uiwait;

%--------------------------------
% Callbacks for user interactions with GUI
%--------------------------------
    function varargout = get_file_call(varargin)
        %----------------------------------------------------
        % Callback for Controller Data File
        %----------------------------------------------------
        [FileName,PathName]=uigetfile('*.mat','Controller Data File');
        set(S.FN_box,'string',FileName);
        try
            addpath(PathName);
        catch %#ok
        end
    end

    function varargout = load_call(varargin)
        %----------------------------------------------------
        % Callback for Loading Data File
        %----------------------------------------------------
        set(S.FNbtn,'enable','off');
        set(S.LDbtn,'enable','off');
        
        temp_FN=get(S.FN_box,'string');
        
        warnflg=0;
        if isempty(temp_FN)
            warndlg({'No file name specified'});
            warnflg=1;
            set(S.FNbtn,'enable','on');
            set(S.LDbtn,'enable','on');
        end
        
        if ~warnflg
            try
                output=load(temp_FN,'inputs');
                output=output.('inputs');
                check=check_fields(output); % 0 if data is invalid

                display(['Controller data ttectra_in loaded from file ' temp_FN])
                close(S.fh);
            catch ME
                errordlg(['Error loading file: ' ME.message]);
                set(S.FNbtn,'enable','on');
                set(S.LDbtn,'enable','on');
            end
        end
    end

    function chk=check_fields(in)
        %----------------------------------------------------
        % Callback for Checking Data File -- check that fields required for
        % successful CL simulation are loaded in selected variable
        %----------------------------------------------------
        if isstruct(in)
            % list of fields (and subfields, etc) required to run simulation
            % without default assignments
            FieldNames={'in','controller','SMLimit','SP','gains','Limiter'};
            SubfieldNames={ {'alt','MN','dTamb','simFileName'}, ...
                            {'FdbkFilterBW','PreFilterBW','bandwidth','phasemargin'}, ...
                            {'Accel','Decel'}, ...
                            {'FT_bkpt','SP','wf_idle','wf_takeoff','NcR25_min','NcR25_max'}, ...
                            {'Fdbk','Kp','Ki'}, ...
                            {'HPC_Limiter','LPC_Limiter','SMLimit'} };           
            SubsubfieldNames={'NcR25_sched','Ncdot_sched'};

            isFN=isfield(in,FieldNames);    % check first that main fields exist
            chk=isFN;

            for i=1:1:length(isFN)
                if ~isFN(i) % warn field doesn't exist
                    warndlg({['Data does not contain field ' FieldNames{i}];'Simulation may not run as expected'});
                else    % check that subfields exit
                    isSF=isfield(in.(FieldNames{i}),SubfieldNames{i});

                    for j=1:1:length(isSF)
                        if ~isSF(j) % warn subfield doesn't exist
                            if i==1 % parameters in "in" must be defined before simulation
                                warndlg({['Field ' FieldNames{i} ' does not contain subfield ' SubfieldNames{i}{j}];[SubfieldNames{i}{j} ' must be defined before running simulation']});
                            end
                            chk(i)=0;
                        end
                        
                        % check sub-subfields in only case they exist
                        if i==length(isFN) && j==i
                            if sum(isfield(in.(FieldNames{i}).(SubfieldNames{j}),SubsubfieldNames))==0
                                warndlg({'Data does not contain acceleration schedule';'Simulation may not run as expected'});
                                chk(i)=0;
                            end
                        end

                        if i==length(isFN) && j==length(isSF) && isSF(j) == 1
                            output.SMLimit=output.Limiter.SMLimit;
                        end
                    end
                end
            end
        else
            warndlg({'Data not valid -- Should be structure containing controller data'});
            chk=zeros(1,6);
        end
    end

    function close_gui_call(src,evnt)
        %----------------------------------------------------
        % Callback for closing GUI -- assign empty output and force
        % controller design if closed without loading data
        %----------------------------------------------------
        if ~exist('output','var')
            output=[];
        end
        
        if ~exist('check','var')
            check=zeros(1,6);
        end
        
        delete(gcf);
    end
end