function [output] = TTECTrA_controller_setup_gui(inputs)
%       TTECTrA.m
%**************************************************************************
% Written by Jeffrey Csank (NASA)
% NASA Glenn Research Center
% March 18th, 2013
% 
%   This gui file sets the default for the contorller tuning
%**************************************************************************

% Setup initialization flags
if ~exist('init_flag','var')
    init_flag=1; %#ok
    lmflag=0;    % enter linear model file
    var1flag=0;  % linear model variable name
    output=inputs;
end


%--------------------------------
% Configure the GUI window
%--------------------------------
S.fh = figure('units','normalized',...
    'position',[0.5 0.1 0.3 0.4],...
    'menubar','none',...
    'name','TTECTrA: Control Design Setup',...
    'numbertitle','off',...
    'visible','off',...
    'resize','on');

%------ Linear Model Panel ------
S.lmpanel = uipanel('units','normalized',...
    'pos',[0.05 0.55 0.9 0.4],...
    'fontsize',12,...
    'TitlePosition', 'centertop',...
    'Title', 'Linear Model Setup');

S.simsel_lab = uicontrol(S.lmpanel,...
    'Style','text',...
    'unit','normalized',...
    'position',[0.1 0.7 0.2 0.15],...
    'fontsize',10,...
    'HorizontalAlignment','center',...
    'string','Model Selected:');

S.simsel = uicontrol(S.lmpanel,...
    'Style','text',...
    'unit','normalized',...
    'position',[0.1 0.55 0.2 0.15],...
    'fontsize',10,...
    'HorizontalAlignment','center',...
    'string','<none>');

S.simsetup = uicontrol(S.lmpanel,...
    'style','push',...
    'unit','normalized',...
    'position',[0.05 0.05 0.3 0.35],...
    'fontsize',10,...
    'string','Load Linear Model',...
    'enable', 'on',...
    'callback',{@lmsetup_call,S});

S.lmtx(1) = uicontrol(S.lmpanel,...
    'style','text',...
    'unit','normalized',...
    'position',[0.5 0.6 0.2 0.2], ...
    'HorizontalAlignment','center',...
    'fontsize',9, ...
    'String',' LM Variable Name');

S.lmvarinput = uicontrol(S.lmpanel,...
    'style','edit',...
    'unit','normalized',...
    'position',[0.75 0.6 0.2 0.2], ...
    'HorizontalAlignment','left',...
    'fontsize',10,...
    'backgroundcolor','white');

S.lmtx(2) = uicontrol(S.lmpanel,...
    'style','text',...
    'unit','normalized',...
    'position',[0.5 0.2 0.2 0.2], ...
    'HorizontalAlignment','center',...
    'fontsize',9, ...
    'String','# Output of LM');

S.outinput = uicontrol(S.lmpanel,...
    'style','edit',...
    'unit','normalized',...
    'position',[0.75 0.2 0.2 0.2], ...
    'HorizontalAlignment','left',...
    'fontsize',10,...
    'backgroundcolor','white');

%------ Controller Tuning Defaults Panel ------
S.defaultpanel = uipanel('units','normalized',...
    'pos',[0.05 0.05 0.9 0.45],...
    'fontsize',12,...
    'TitlePosition', 'centertop',...
    'Title', 'Tuning Defaults');

S.defaulttx(1)=uicontrol(S.defaultpanel,...
    'style','text',...
    'unit','normalized',...
    'position',[0.05 0.75 0.2 0.2],...
    'HorizontalAlignment','center',...
    'fontsize',9, ...
    'String',' Bandwidth (Hz)');
S.bwinput = uicontrol(S.defaultpanel,...
    'style','edit',...
    'unit','normalized',...
    'position',[0.3 0.75 0.1 0.2],...
    'HorizontalAlignment','left',...
    'fontsize',10,...
    'backgroundcolor','white');

S.defaulttx(2)=uicontrol(S.defaultpanel,...
    'style','text',...
    'unit','normalized',...
    'position',[0.55 0.75 0.2 0.2],...
    'HorizontalAlignment','center',...
    'fontsize',9, ...
    'String',' Phase Margin (deg)');
S.pminput = uicontrol(S.defaultpanel,...
    'style','edit',...
    'unit','normalized',...
    'position',[0.8 0.75 0.1 0.2],...
    'HorizontalAlignment','left',...
    'fontsize',10,...
    'backgroundcolor','white');

S.defaulttx(1)=uicontrol(S.defaultpanel,...
    'style','text',...
    'unit','normalized',...
    'position',[0.05 0.5 0.2 0.2],...
    'HorizontalAlignment','center',...
    'fontsize',9, ...
    'String',' Feedback Filter Bandwidth (Hz)');
S.fbinput = uicontrol(S.defaultpanel,...
    'style','edit',...
    'unit','normalized',...
    'position',[0.3 0.5 0.1 0.2],...
    'HorizontalAlignment','left',...
    'fontsize',10,...
    'backgroundcolor','white');

S.defaulttx(2)=uicontrol(S.defaultpanel,...
    'style','text',...
    'unit','normalized',...
    'position',[0.55 0.5 0.2 0.2],...
    'HorizontalAlignment','center',...
    'fontsize',9, ...
    'String',' PreFilter Bandwidth (Hz)');
S.pfinput = uicontrol(S.defaultpanel,...
    'style','edit',...
    'unit','normalized',...
    'position',[0.8 0.5 0.1 0.2],...
    'HorizontalAlignment','left',...
    'fontsize',10,...
    'backgroundcolor','white');

S.setup = uicontrol(S.defaultpanel,...
    'style','push',...
    'unit','normalized',...
    'position',[0.25 0.05 0.5 0.35],...
    'fontsize',10,...
    'string','Start',...
    'enable', 'on',...
    'callback',{@setup_call,S});

%--------------------------------------------------------------------------
% Initialize: populate text boxes with pre-defined values when GUI is
%             opened, for any field provided in inputs
%--------------------------------------------------------------------------

%--- Initialize tuning defaults
if isfield(inputs.controller,'FdbkFilterBW')
    set(S.fbinput,'String',num2str(inputs.controller.FdbkFilterBW));
end

if isfield(inputs.controller,'PreFilterBW')
    set(S.pfinput,'String',num2str(inputs.controller.PreFilterBW));
else
    set(S.pfinput,'String',num2str(3));
end

if isfield(inputs.controller,'bandwidth')
    set(S.bwinput,'String',num2str(inputs.controller.bandwidth));
else
    set(S.bwinput,'String',num2str(1.5));
end

if isfield(inputs.controller,'phasemargin')
    set(S.pminput,'String',num2str(inputs.controller.phasemargin));
else
    set(S.pminput,'String',num2str(45));
end

%--- Initialize linear model references
if isfield(inputs.controller,'lmVar')
    set(S.lmvarinput,'String',inputs.controller.lmVar);
    var1flag=1.0;
end

if isfield(inputs.controller,'LMFileName')
    set(S.simsel,'String',inputs.controller.LMFileName);
    lmflag=1.0;
end

if isfield(inputs.controller,'CVoutput')
    set(S.outinput,'String',num2str(inputs.controller.CVoutput));
else
    set(S.outinput,'String',num2str(1));
end

%--- move and show the gui
movegui(S.fh,'center')
set(S.fh,'visible','on'); % Make the GUI visible.

uiwait;

%--------------------------------
% Callbacks for user interactions with GUI
%--------------------------------
    function varargout = lmsetup_call(varargin)
        %----------------------------------------------------
        % Callback for Linear Model File
        %----------------------------------------------------
        [FileName,PathName]=uigetfile('*.mat','Linear Model File');
        
        output.controller.LMFileName=FileName;
        set(S.simsel,'string',output.controller.LMFileName);
        try
            addpath(PathName);
        catch %#ok
        end
        lmflag=1;
    end

    function varargout = setup_call(varargin)
        %----------------------------------------------------
        % Callback to load inputs
        %----------------------------------------------------
        
        % linearized model, default controller information
        output.controller.FdbkFilterBW=str2double(get(S.fbinput,'string'));
        output.controller.PreFilterBW=str2double(get(S.pfinput,'string'));
        output.controller.CVoutput=str2double(get(S.outinput,'string'));
        output.controller.bandwidth=str2double(get(S.bwinput,'string'));
        output.controller.phasemargin=str2double(get(S.pminput,'string'));
        
        temp1=get(S.lmvarinput,'string');
        
        if ~isempty(temp1)
            output.controller.lmVar=get(S.lmvarinput,'string');
            var1flag=1;
        elseif isempty(temp1)
            var1flag=0;
        end
        
        if lmflag==0  
            warndlg({'Enter Linear Model File'});
        end

        if var1flag==0
            warndlg({'Enter Linear Model Variable Name'});
        end
  
        if lmflag==1 && var1flag==1
            close all;
            return;
        end
        
    end
end

