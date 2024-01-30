function save_ctrl_gui(inputs)
%       save_ctrl_gui.m
%********************************************************************
% Written by Alicia Zinnecker (N&R Engineering) and Jeffrey Csank (NASA)
% NASA Glenn Research Center, Cleveland, OH
% May 2013
%
% This file prompts the user to save the designed controller for use in
% subsequent simulations without needed to re-design it.
%********************************************************************

close all;

%--------------------------------
% Configure the GUI window
%--------------------------------
S.fh = figure('units','normalized',...
    'position',[.1 .2 .2 .1],...
    'menubar','none',...
    'name','TTECTrA: Save Controller Data',...
    'numbertitle','off',...
    'visible','off',...
    'resize','on');

S.panel1 = uipanel('units','normalized',...
    'pos',[.01 .01 .98 .98],...
    'fontsize',12,...
    'TitlePosition', 'centertop');

%------ File Name Panel ------
S.lmtx = uicontrol(S.panel1,...
    'style','text',...
    'unit','normalized',...
    'position',[.05 .55 .25 .3],...
    'HorizontalAlignment','center',...
    'fontsize',9, ...
    'String','File name');

S.FN_box = uicontrol(S.panel1,...
    'style','edit',...
    'unit','normalized',...
    'position',[0.40 0.60 0.5 0.35],...
    'HorizontalAlignment','left',...
    'fontsize',10,...
    'backgroundcolor','white');

S.SVbtn = uicontrol(S.panel1,...
    'style','push',...
    'unit','normalized',...
    'position',[.10 .10 .80 .30],...
    'fontsize',10,...
    'string','Save Data',...
    'enable', 'on',...
    'callback',{@save_call,S});

%--- move and show the gui
movegui(S.fh,'center');
set(S.fh,'visible','on'); % Make the GUI visible.

uiwait;

%--------------------------------
% Callbacks for user interactions with GUI
%--------------------------------
    function varargout = save_call(varargin)
        %----------------------------------------------------
        % Callback for Saving Data File
        %----------------------------------------------------
        set(S.SVbtn,'enable','off');
        
        temp_FN=get(S.FN_box,'string');
        
        if isempty(temp_FN)
            warndlg({'No file name specified'});
            set(S.SVbtn,'enable','on');
        else
            FN=[temp_FN '_' date '.mat'];
            
            save(FN,'inputs');
            display(['Controller data saved in file ' FN])
        
            close(gcf);
        end
    end
end