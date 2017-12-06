classdef SoundSource < handle
    %SoundSource is class with information about sound source
    
    %   create new object using constructor (needs name of sound source)
    %   enter all properties of sound source and save info into mat-file by
    %   using save method.
    %   for example:
    %      S1 = SoundSource('MAG111');
    %      S1.weight = 6;
    %      ..............
    %      S1.save('C:\User\Desktop');
    
    %   Also you can plot some properties of ound source by using visualize
    %   method. just type property that ou want  to visualize like argument
    %   for example:
    %      S1.visualize('impedance');
    
    properties(Constant)
        f = [16 20 25 31.5 40 50 63 80 100 125 ...
            160 200 250 315 400 500 630 800 1e3 1.25e3 ...
            1.6e3 2e3 2.5e3 3.15e3 4e3 5e3 6.3e3 8e3 1e4 1.25e4 ...
            1.6e4 2e4];
    end
    
    properties
        name;
        %sizes = [0.5 0.5 0.5];
        weight = 15;
        additInfo = cell(1,10);
        impedance;
        sensitivity;
        amplitudeRP;
        phaseRP;
        CP = [0.5 0.25 0.25];
    end
    
    properties (SetObservable)
        sizes = [0.5 0.5 0.5];
    end
    
    methods
        %% constructor
        function obj = SoundSource(name)
            if nargin ~= 1
                error('Enter source name');
            else
                obj.name = name;
            end
            
            addlistener(obj, 'sizes', 'PostSet', @(src,eventData)obj.chngCP);
        end
        
        %% method for visualization of some properties      
        function visualize(obj, prop, freq)
            if nargin < 2
                error('Enter name of parameter for visualization');
            end
            if nargin < 3
                freq = 0;
            end
            
            switch prop
                
                case 'impedance'
                    if numel(obj.impedance) ~= length(obj.f)
                        error('Enter values of impedance for all valid central frequencies of 1/3 octave bands from 16 Hz to 20 kHz');
                    end
                    
                    figure();
                    if ~isreal(obj.impedance)
                        subplot(2,1,1); plot(obj.f, abs(obj.impedance));
                                        xlabel('f, Hz'); ylabel('|Z|, Ohm'); grid on; title('Amplitude of impedance');
                        subplot(2,1,2); plot(obj.f, phase(obj.impedance));
                                        xlabel('f, Hz'); ylabel('/Phi, Rad'); grid on; title('Phase of impedance');
                    else
                        plot(obj.f, obj.impedance);
                        xlabel('f, Hz'); ylabel('|Z|, Ohm'); grid on; title('Amplitude of impedance');
                    end
                    
                case 'sensitivity'
                    if numel(obj.sensitivity) ~= length(obj.f)
                        error('Enter values of sensitivity for all valid central frequencies of 1/3 octave bands from 16 Hz to 20 kHz');
                    end
                    
                    figure();
                    semilogy(obj.f, obj.impedance);
                    xlabel('f, Hz'); ylabel('SPL, dB/m/W'); grid on; title('Sensitivity');
                    
                case 'amplitudeRP'
                    if nargin < 3 || isempty(find(obj.f == freq, 1))
                        error('Enter valid frequency for RP visualization');
                    end
                    if (sum(size(obj.amplitudeRP) == [360 181 32]) ~= 3) || (~isreal(obj.amplitudeRP))
                        error('Enter values of amplitude RP for all angles from 1 to 360 in two dimensionals (you must enter tensor with size 360x181x32)');
                    end
                    
                    figure();
                    visualizeGroup = hggroup;
                    Phi = linspace(0, pi*2, 360);
                    Theta = linspace(0, pi, 181);
                    [Phi, Theta] = meshgrid(Phi, Theta);
                    RP = obj.amplitudeRP(:,:,find(obj.f == freq, 1));
                    RP = RP - min(min(RP)); % we can't use negative values for plotting
                    RP = RP ./ max(max(RP)) .* (min(obj.sizes)/2); % normalization to min size of box
                    
                    Xx = obj.sizes(1);
                    Yy = obj.sizes(2);
                    Zz = obj.sizes(3);
                    x = [0 Xx Xx 0 0 Xx Xx 0];
                    y = [0 0 Yy Yy 0 0 Yy Yy] ;
                    z = x'*x*(y')*y/Yy^2/Xx^2*Zz/2;
                    h1 = surf(x, y, z, ones(size(z)), 'Parent', visualizeGroup);
                    alpha(h1, 0.2);
                    hold on;
                    axis equal;
                    
                    [X, Y, Z] = sph2cart(Theta, Phi, RP);
                    X = X + obj.CP(1);
                    Y = Y + obj.CP(2);
                    Z = Z + obj.CP(3);
                    h2 = surf(X, Y, Z, 'Parent', visualizeGroup);
                    alpha(h2, 1);
                    %shading(h2, 'interp');
                    h2.EdgeColor = 'none';
                    
                    xlabel('x, m'); ylabel('y, m'); zlabel('z, m');
                    %shading interp;
                    hold off;
                    view([120 25]);
                    
                    obj.additInfo{1,2} = visualizeGroup;
                case 'phaseRP'
                    if nargin < 3 || isempty(find(obj.f == freq, 1))
                        error('Enter valid frequency for RP visualization');
                    end
                    if (sum(size(obj.phaseRP) == [360 181 32]) ~= 3) || (~isreal(obj.phaseRP))
                        error('Enter values of phase RP for all angles from 1 to 360 in two dimensionals (you must enter tensor with size 360x181x32)');
                    end
                    
                    figure();
                    Phi = linspace(0, pi*2, 360);
                    Theta = linspace(0, pi, 181);
                    [Phi, Theta] = meshgrid(Phi, Theta);
                    [X, Y, Z] = sph2cart(Theta, Phi, obj.phaseRP(:,:,find(obj.f == freq, 1)));
                    surf(X,Y,Z);
                    xlabel('x'); ylabel('y'); zlabel('z');
                    shading interp;
                    
                otherwise
                    error('Enter valid property of sound source');          
            end
        end
        
        %% save data to mat
        function save(obj,filePath)
            if nargin ~= 2
                error('Enter path for saving file');
            end
            
            if isempty(obj.impedance) || isempty(obj.sensitivity) || isempty(obj.amplitudeRP) ...
                    || isempty(obj.phaseRP)
                eroor('Fill in all values of sound source properties');
            end
            
            if (numel(obj.sizes) ~= 3) || (~isempty(find(obj.sizes <= 0, 1))) || (~isreal(obj.sizes))
                error('Sound source sizes is not a valid value');
            end
            
            if (numel(obj.weight) ~= 1) || (obj.weight <= 0) || (~isreal(obj.weight))
                error('Sound source weight is not a valid value');
            end
            
            if numel(obj.impedance) ~= length(obj.f)
                error('Sound source impedance is not a valid value');
            end
            
            if numel(obj.sensitivity) ~= length(obj.f)
                error('Sound source sensitivity is not a valid value');
            end
            
            if (sum(size(obj.amplitudeRP) == [360 181 32]) ~= 3) || (~isreal(obj.amplitudeRP))
                error('Sound source amplitude RP is not a valid value (you must enter tensor with size 360x360x32 of real numbers)');
            end
            
            if (sum(size(obj.phaseRP) == [360 181 32]) ~= 3) || (~isreal(obj.phaseRP))
                error('Sound source phase RP is not a valid value (you must enter tensor with size 360x360x32 of real numbers)');
            end
            
            if ~isempty(obj.additInfo)
                obj.additInfo{1,1} = ['Last modified ',datestr(datetime('now'))];
            end

            
            if ~exist(filePath, 'dir')
                mkdir(filePath);
            end
            fileName = fullfile(filePath,[obj.name,'.mat']);
            save(fileName, 'obj');
        end
        
%         %% rotate method
%         function upend(obj)
%             zOld = obj.sizes(3);
%             xOld = obj.sizes(1);
%             obj.sizes(1) = zOld;
%             obj.sizes(3) = xOld;
%             obj.amplitudeRP = permute(obj.amplitudeRP, [2 1 3]);
%             obj.phaseRP = permute(obj.phaseRP, [2 1 3]);
%         end
        
        %% calculation of Central Point
        function chngCP(obj)
            obj.CP = obj.sizes .* [1 0.5 0.5];
        end
        
    end
     
end

