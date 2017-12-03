classdef SoundSource
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
        size = [0.5 0.5 0.5];
        weight = 15;
        additInfo = cell(1,10);
        impedance;
        sensitivity;
        amplitudeRP;
        phaseRP;
    end
    
    methods
        %% constructor
        function obj = SoundSource(name)
            if nargin ~= 1
                error('Enter source name');
            else
                obj.name = name;
            end
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
                    if (numel(obj.amplitudeRP) ~= 360*360*32) || (~isreal(obj.amplitudeRP))
                        error('Enter values of amplitude RP for all angles from 1 to 360 in two dimensionals/n(you must enter matrix with size 360x360)');
                    end
                    
                    
                    figure();
                    Phi = linspace(0, pi*2, 360);
                    Theta = linspace(0, pi*2, 360);
                    [Phi, Theta] = meshgrid(Phi, Theta);
                    [X, Y, Z] = sph2cart(Theta, Phi, obj.amplitudeRP(find(obj.f == freq, 1)));
                    surf(X,Y,Z);
                    shading interp;
                    
                case 'phaseRP'
                    if nargin < 3 || isempty(find(obj.f == freq, 1))
                        error('Enter valid frequency for RP visualization');
                    end
                    if (numel(obj.phaseRP) ~= 360*360*32) || (~isreal(obj.phaseRP))
                        error('Enter values of phase RP for all angles from 1 to 360 in two dimensionals/n(you must enter matrix with size 360x360)');
                    end
                    
                    figure();
                    Phi = linspace(0, pi*2, 360);
                    Theta = linspace(0, pi*2, 360);
                    [Phi, Theta] = meshgrid(Phi, Theta);
                    [X, Y, Z] = sph2cart(Theta, Phi, obj.phaseRP(find(obj.f == freq, 1)));
                    surf(X,Y,Z);
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
            sourceData.name = obj.name;
            
            if (numel(obj.size) ~= 3) || (~isempty(find(obj.size <= 0, 1))) || (~isreal(obj.size))
                error('Sound source size is not a valid value');
            end
            sourceData.size = obj.size;
            
            if (numel(obj.weight) ~= 1) || (obj.weight <= 0) || (~isreal(obj.weight))
                error('Sound source weight is not a valid value');
            end
            sourceData.weight = obj.weight;
            
            if numel(obj.impedance) ~= length(obj.f)
                error('Sound source impedance is not a valid value');
            end
            sourceData.impedance = obj.impedance;
            
            if numel(obj.sensitivity) ~= length(obj.f)
                error('Sound source sensitivity is not a valid value');
            end
            sourceData.sensitivity = obj.sensitivity;
            
            if (numel(obj.amplitudeRP) ~= 360*360) || (~isreal(obj.amplitudeRP))
                error('Sound source amplitude RP is not a valid value');
            end
            sourceData.amplitudeRP = obj.amplitudeRP;
            
            if (numel(obj.phaseRP) ~= 360*360) || (~isreal(obj.phaseRP))
                error('Sound source phase RP is not a valid value');
            end
            sourceData.phaseRP = obj.phaseRP;
            
            if ~isempty(obj.additInfo)
                obj.additInfo{1,1} = ['Last modified ',datestr(datetime('now'))];
                sourceData.additInfo = obj.additInfo;
            end

            
            if ~exist(filePath, 'dir')
                mkdir(filePath);
            end
            fileName = fullfile(filePath,[obj.name,'.mat']);
            save(fileName, 'sourceData');
        end
    end
    
end

