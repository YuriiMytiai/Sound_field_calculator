function varargout = main(varargin)
% MAIN MATLAB code for main.fig
%      MAIN, by itself, creates a new MAIN or raises the existing
%      singleton*.
%
%      H = MAIN returns the handle to a new MAIN or the handle to
%      the existing singleton*.
%
%      MAIN('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in MAIN.M with the given input arguments.
%
%      MAIN('Property','Value',...) creates a new MAIN or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before main_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to main_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help main

% Last Modified by GUIDE v2.5 06-Dec-2017 10:13:19

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @main_OpeningFcn, ...
                   'gui_OutputFcn',  @main_OutputFcn, ...
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


% --- Executes just before main is made visible.
function main_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to main (see VARARGIN)

% Choose default command line output for main
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes main wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = main_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;


% --- Executes on button press in areaConstr.
function areaConstr_Callback(hObject, eventdata, handles)
% hObject    handle to areaConstr (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
global area

waitfor(AreaParameters(figure(AreaParameters)));
Xgrid = area.grid.X;
Ygrid = area.grid.Y;
Z = zeros(size(Xgrid));
C(:,:,1) = zeros(size(Z)) + 1;
C(:,:,2) = zeros(size(Z)) + 0;
C(:,:,3) = zeros(size(Z)) + 1;

axes(handles.axes1);
surf(Xgrid, Ygrid, Z, C, 'Parent', handles.axes1);
xlabel('x, m'); ylabel('y, m'); zlabel('z, m');
axis equal;
rotate3d on;

% --- Executes on button press in addSource.
function addSource_Callback(hObject, eventdata, handles)
% hObject    handle to addSource (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
global area

waitfor(AddSource(figure(AddSource)));

notEmptyCells = (find(~cellfun('isempty', area.Sources)));
NumSources  = numel(notEmptyCells);

% refresh list of added sources
listboxItems = cell(NumSources,1);
for curS = 1:NumSources
    nameS = [area.Sources{notEmptyCells(curS)}.SSobj.name, '_', num2str(notEmptyCells(curS))];
    listboxItems{curS,1} = nameS;
end
set(handles.listbox1, 'String', listboxItems);

%updateAxes1(area);
axes(handles.axes1);
cla(handles.axes1);
Xgrid = area.grid.X;
Ygrid = area.grid.Y;
Z = zeros(size(Xgrid));
C(:,:,1) = zeros(size(Z)) + 1;
C(:,:,2) = zeros(size(Z)) + 0;
C(:,:,3) = zeros(size(Z)) + 1;
surf(Xgrid, Ygrid, Z, C, 'Parent', handles.axes1);
xlabel('x, m'); ylabel('y, m'); zlabel('z, m');
axis equal;
rotate3d on;

hold on;
for curS = 1:NumSources
    obj = area.Sources{curS}.SSobj;
    
%     Ry = roty(area.Sources{curS}.theta0);
%     Rz = roty(area.Sources{curS}.phi0);
    Xx = obj.sizes(1);
    Yy = obj.sizes(2);
    Zz = obj.sizes(3);
%     newCoordsBox1 = Rz * [Xx; Yy; Zz];
%     newCoordsBox2 = Ry * newCoordsBox1;
%     Xx = newCoordsBox2(1);
%     Yy = newCoordsBox2(2);
%     Zz = newCoordsBox2(3);
    
    x = [0 Xx Xx 0 0 Xx Xx 0];
    y = [0 0 Yy Yy 0 0 Yy Yy] ;
    z = x'*x*(y')*y/Yy^2/Xx^2*Zz/2;
    x = x + area.Sources{curS}.position(1) - obj.CP(1);
    y = y + area.Sources{curS}.position(2) - obj.CP(2);
    z = z + area.Sources{curS}.position(3) - obj.CP(3);
    
    h1 = surf(x, y, z, ones(size(z)));
    alpha(h1, 0.2);

    Phi = linspace(0, pi*2, 360)';
    Theta = linspace(0, pi, 181);
    [Phi, Theta] = meshgrid(Phi, Theta);
    RP = obj.amplitudeRP(:,:,find(obj.f == 1e3, 1));
    RP = RP - min(min(RP)); % we can't use negative values for plotting
    RP = RP ./ max(max(RP)) .* (min(obj.sizes)/2); % normalization to min size of box

    
    [X, Y, Z] = sph2cartCustom(Phi', Theta', RP);
    X = X + area.Sources{curS}.position(1);
    Y = Y + area.Sources{curS}.position(2);
    Z = Z + area.Sources{curS}.position(3);
    
%     newCoordsRP1 = Rz * [X; Y; Z];
%     newCoordsRP2 = Ry * newCoordsRP1;
%     X = newCoordsRP2(1);
%     Y = newCoordsRP2(2);
%     Z = newCoordsRP2(3);
    
    h2 = surf(X, Y, Z);
    alpha(h2, 1);
    h2.EdgeColor = 'none';
    
    if area.Sources{curS}.phi0 ~= 0
        zDir = [0 0 1];
        rotate(h1, zDir, area.Sources{curS}.phi0, obj.CP);
        rotate(h2, zDir, area.Sources{curS}.phi0, obj.CP);
    end
    if area.Sources{curS}.theta0 ~= 0
        yDir = [0 1 0];
        rotate(h1, yDir, area.Sources{curS}.theta0, obj.CP);
        rotate(h2, yDir, area.Sources{curS}.theta0, obj.CP);
    end

    
end
hold off





% --- Executes on selection change in listbox1.
function listbox1_Callback(hObject, eventdata, handles)
% hObject    handle to listbox1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: contents = cellstr(get(hObject,'String')) returns listbox1 contents as cell array
%        contents{get(hObject,'Value')} returns selected item from listbox1


% --- Executes during object creation, after setting all properties.
function listbox1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to listbox1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: listbox controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in modifySource.
function modifySource_Callback(hObject, eventdata, handles)
% hObject    handle to modifySource (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)


% --- Executes on button press in delSource.
function delSource_Callback(hObject, eventdata, handles)
% hObject    handle to delSource (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)


% --- Executes on button press in sourceField.
function sourceField_Callback(hObject, eventdata, handles)
% hObject    handle to sourceField (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)


% --- Executes on button press in addReciever.
function addReciever_Callback(hObject, eventdata, handles)
% hObject    handle to addReciever (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)


% --- Executes on button press in showSumField.
function showSumField_Callback(hObject, eventdata, handles)
% hObject    handle to showSumField (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

function updateAxes1(area)
