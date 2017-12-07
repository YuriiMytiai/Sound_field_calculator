function varargout = ModifySource(varargin)
% MODIFYSOURCE MATLAB code for ModifySource.fig
%      MODIFYSOURCE, by itself, creates a new MODIFYSOURCE or raises the existing
%      singleton*.
%
%      H = MODIFYSOURCE returns the handle to a new MODIFYSOURCE or the handle to
%      the existing singleton*.
%
%      MODIFYSOURCE('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in MODIFYSOURCE.M with the given input arguments.
%
%      MODIFYSOURCE('Property','Value',...) creates a new MODIFYSOURCE or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before ModifySource_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to ModifySource_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help ModifySource

% Last Modified by GUIDE v2.5 06-Dec-2017 11:26:57

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @ModifySource_OpeningFcn, ...
                   'gui_OutputFcn',  @ModifySource_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before ModifySource is made visible.
function ModifySource_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to ModifySource (see VARARGIN)

% Choose default command line output for ModifySource
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

global area sourceNum

set(handles.xPoint, 'String', num2str(area.Sources{sourceNum}.position(1)));
set(handles.yPoint, 'String', num2str(area.Sources{sourceNum}.position(2)));
set(handles.zPoint, 'String', num2str(area.Sources{sourceNum}.position(3)));
set(handles.phi, 'String', num2str(-area.Sources{sourceNum}.phi0));
set(handles.theta, 'String', num2str(-area.Sources{sourceNum}.theta0));
set(handles.gain, 'String', num2str(area.Sources{sourceNum}.K));
set(handles.tau, 'String', num2str(area.Sources{sourceNum}.tau0));




% UIWAIT makes ModifySource wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = ModifySource_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;





function xPoint_Callback(hObject, eventdata, handles)
% hObject    handle to xPoint (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of xPoint as text
%        str2double(get(hObject,'String')) returns contents of xPoint as a double


% --- Executes during object creation, after setting all properties.
function xPoint_CreateFcn(hObject, eventdata, handles)
% hObject    handle to xPoint (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function yPoint_Callback(hObject, eventdata, handles)
% hObject    handle to yPoint (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of yPoint as text
%        str2double(get(hObject,'String')) returns contents of yPoint as a double


% --- Executes during object creation, after setting all properties.
function yPoint_CreateFcn(hObject, eventdata, handles)
% hObject    handle to yPoint (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function zPoint_Callback(hObject, eventdata, handles)
% hObject    handle to zPoint (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of zPoint as text
%        str2double(get(hObject,'String')) returns contents of zPoint as a double


% --- Executes during object creation, after setting all properties.
function zPoint_CreateFcn(hObject, eventdata, handles)
% hObject    handle to zPoint (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function phi_Callback(hObject, eventdata, handles)
% hObject    handle to phi (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of phi as text
%        str2double(get(hObject,'String')) returns contents of phi as a double


% --- Executes during object creation, after setting all properties.
function phi_CreateFcn(hObject, eventdata, handles)
% hObject    handle to phi (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function theta_Callback(hObject, eventdata, handles)
% hObject    handle to theta (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of theta as text
%        str2double(get(hObject,'String')) returns contents of theta as a double


% --- Executes during object creation, after setting all properties.
function theta_CreateFcn(hObject, eventdata, handles)
% hObject    handle to theta (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function gain_Callback(hObject, eventdata, handles)
% hObject    handle to gain (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of gain as text
%        str2double(get(hObject,'String')) returns contents of gain as a double


% --- Executes during object creation, after setting all properties.
function gain_CreateFcn(hObject, eventdata, handles)
% hObject    handle to gain (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in applyBut.
function applyBut_Callback(hObject, eventdata, handles)
% hObject    handle to applyBut (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

global area sourceNum

if isempty(area)
    errordlg('Please, set area parameters before adding sources', 'Create area');
    return
end


xPosition = str2double(get(handles.xPoint, 'String'));
yPosition = str2double(get(handles.yPoint, 'String'));
zPosition = str2double(get(handles.zPoint, 'String'));

if isnan(xPosition) || isnan(yPosition) || isnan(zPosition)...
        || (~isreal(xPosition)) || (~isreal(yPosition)) || (~isreal(zPosition))
    errordlg('Please, enter a valid position values', 'Invalid values');
    return
end


phi0 = str2double(get(handles.phi, 'String'));
theta0 = str2double(get(handles.theta, 'String'));
K = str2double(get(handles.gain, 'String'));

if isnan(phi0) || isnan(theta0) || isnan(K)...
        || (~isreal(phi0)) || (~isreal(theta0)) || (~isreal(K))
    errordlg('Please, enter a valid source parameters', 'Invalid values');
    return
end


tau0 = str2double(get(handles.tau, 'String'));
if isnan(tau0) || (~isreal(tau0)) || (tau0 > 0)
    errordlg('Please, enter a valid delay value', 'Invalid value');
    return
end

area.Sources{sourceNum}.phi0 = -phi0;
area.Sources{sourceNum}.theta0 = -theta0;
area.Sources{sourceNum}.tau0 = tau0;
area.Sources{sourceNum}.K = K;
area.Sources{sourceNum}.position = [xPosition, yPosition, zPosition];

close(ModifySource);

function tau_Callback(hObject, eventdata, handles)
% hObject    handle to tau (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tau as text
%        str2double(get(hObject,'String')) returns contents of tau as a double


% --- Executes during object creation, after setting all properties.
function tau_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tau (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
