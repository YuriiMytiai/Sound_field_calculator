function varargout = AreaParameters(varargin)
% AREAPARAMETERS MATLAB code for AreaParameters.fig
%      AREAPARAMETERS, by itself, creates a new AREAPARAMETERS or raises the existing
%      singleton*.
%
%      H = AREAPARAMETERS returns the handle to a new AREAPARAMETERS or the handle to
%      the existing singleton*.
%
%      AREAPARAMETERS('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in AREAPARAMETERS.M with the given input arguments.
%
%      AREAPARAMETERS('Property','Value',...) creates a new AREAPARAMETERS or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before AreaParameters_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to AreaParameters_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help AreaParameters

% Last Modified by GUIDE v2.5 06-Dec-2017 10:18:26

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @AreaParameters_OpeningFcn, ...
                   'gui_OutputFcn',  @AreaParameters_OutputFcn, ...
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


% --- Executes just before AreaParameters is made visible.
function AreaParameters_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to AreaParameters (see VARARGIN)

% Choose default command line output for AreaParameters
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes AreaParameters wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = AreaParameters_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;



function xSize_Callback(hObject, eventdata, handles)
% hObject    handle to xSize (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of xSize as text
%        str2double(get(hObject,'String')) returns contents of xSize as a double


% --- Executes during object creation, after setting all properties.
function xSize_CreateFcn(hObject, eventdata, handles)
% hObject    handle to xSize (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function xStep_Callback(hObject, eventdata, handles)
% hObject    handle to xStep (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of xStep as text
%        str2double(get(hObject,'String')) returns contents of xStep as a double


% --- Executes during object creation, after setting all properties.
function xStep_CreateFcn(hObject, eventdata, handles)
% hObject    handle to xStep (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function ySize_Callback(hObject, eventdata, handles)
% hObject    handle to ySize (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of ySize as text
%        str2double(get(hObject,'String')) returns contents of ySize as a double


% --- Executes during object creation, after setting all properties.
function ySize_CreateFcn(hObject, eventdata, handles)
% hObject    handle to ySize (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function yStep_Callback(hObject, eventdata, handles)
% hObject    handle to yStep (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of yStep as text
%        str2double(get(hObject,'String')) returns contents of yStep as a double


% --- Executes during object creation, after setting all properties.
function yStep_CreateFcn(hObject, eventdata, handles)
% hObject    handle to yStep (see GCBO)
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
global area

xSize = str2double(get(handles.xSize, 'String'));
ySize = str2double(get(handles.ySize, 'String'));
xStep = str2double(get(handles.xStep, 'String'));
yStep = str2double(get(handles.yStep, 'String'));
if isnan(xSize) || isnan(ySize) || isnan(xStep) || isnan(yStep)...
        || (xSize <= 0) || (ySize <= 0) || (xStep <= 0) || (yStep <= 0)...
        || (~isreal(xSize)) || (~isreal(ySize)) || (~isreal(xStep)) || (~isreal(yStep))...
        || (xStep >= xSize) || (yStep >= ySize)
    errordlg('Please, enter a valid values', 'Invalid values');
    return
end

area = Area(xSize, ySize, xStep, yStep);
close(AreaParameters);
