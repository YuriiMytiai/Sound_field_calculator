function varargout = SelectFrequency(varargin)
% SELECTFREQUENCY MATLAB code for SelectFrequency.fig
%      SELECTFREQUENCY, by itself, creates a new SELECTFREQUENCY or raises the existing
%      singleton*.
%
%      H = SELECTFREQUENCY returns the handle to a new SELECTFREQUENCY or the handle to
%      the existing singleton*.
%
%      SELECTFREQUENCY('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in SELECTFREQUENCY.M with the given input arguments.
%
%      SELECTFREQUENCY('Property','Value',...) creates a new SELECTFREQUENCY or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before SelectFrequency_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to SelectFrequency_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help SelectFrequency

% Last Modified by GUIDE v2.5 06-Dec-2017 12:32:20

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @SelectFrequency_OpeningFcn, ...
                   'gui_OutputFcn',  @SelectFrequency_OutputFcn, ...
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


% --- Executes just before SelectFrequency is made visible.
function SelectFrequency_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to SelectFrequency (see VARARGIN)

% Choose default command line output for SelectFrequency
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

global freq
if ~isempty(freq)
    set(handles.freq, 'String', num2str(freq));
end

% UIWAIT makes SelectFrequency wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = SelectFrequency_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;



function freq_Callback(hObject, eventdata, handles)
% hObject    handle to freq (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of freq as text
%        str2double(get(hObject,'String')) returns contents of freq as a double


% --- Executes during object creation, after setting all properties.
function freq_CreateFcn(hObject, eventdata, handles)
% hObject    handle to freq (see GCBO)
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
global freq

f = [16 20 25 31.5 40 50 63 80 100 125 ...
            160 200 250 315 400 500 630 800 1e3 1.25e3 ...
            1.6e3 2e3 2.5e3 3.15e3 4e3 5e3 6.3e3 8e3 1e4 1.25e4 ...
            1.6e4 2e4];

frequency = str2double(get(handles.freq, 'String'));
 
if isempty(find(frequency == f, 1))
   errordlg('Please, enter valid frequency value from 1/3 octave range', 'Invalid value');
   return
end

freq = frequency;
 
close(SelectFrequency);