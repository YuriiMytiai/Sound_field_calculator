function varargout = AddReciever(varargin)
% ADDRECIEVER MATLAB code for AddReciever.fig
%      ADDRECIEVER, by itself, creates a new ADDRECIEVER or raises the existing
%      singleton*.
%
%      H = ADDRECIEVER returns the handle to a new ADDRECIEVER or the handle to
%      the existing singleton*.
%
%      ADDRECIEVER('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in ADDRECIEVER.M with the given input arguments.
%
%      ADDRECIEVER('Property','Value',...) creates a new ADDRECIEVER or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before AddReciever_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to AddReciever_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help AddReciever

% Last Modified by GUIDE v2.5 06-Dec-2017 10:40:38

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @AddReciever_OpeningFcn, ...
                   'gui_OutputFcn',  @AddReciever_OutputFcn, ...
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


% --- Executes just before AddReciever is made visible.
function AddReciever_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to AddReciever (see VARARGIN)

% Choose default command line output for AddReciever
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes AddReciever wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = AddReciever_OutputFcn(hObject, eventdata, handles) 
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


% --- Executes on button press in applyBut.
function applyBut_Callback(hObject, eventdata, handles)
% hObject    handle to applyBut (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
global area

if isempty(area)
    errordlg('Please, set area parameters before adding sources', 'Create area');
    return
end

xPosition = str2double(get(handles.xPoint, 'String'));
yPosition = str2double(get(handles.yPoint, 'String'));
zPosition = str2double(get(handles.zPoint, 'String'));

if isnan(xPosition) || isnan(yPosition) || isnan(zPosition)...
        || (~isreal(xPosition)) || (~isreal(yPosition)) || (~isreal(zPosition))...
        || (xPosition < 0) || (yPosition < 0) || (zPosition < 0)...
        || (xPosition > area.xSize) || (yPosition > area.ySize)
    errordlg('Please, enter a valid position values (inside areaa)', 'Invalid values');
    return
end

Rcv = Reciever([xPosition, yPosition, zPosition]);

area.addReciever(Rcv);

close(AddReciever);
