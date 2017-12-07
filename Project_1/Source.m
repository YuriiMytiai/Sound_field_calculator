classdef Source < handle
    %Source calss with info about position of SoundSource object in current
    %area
    %   Detailed explanation goes here
    
    properties
        position % [x y z] position in m
        phi0 = 0; % rotation in deg
        theta0 = 0; % incline in deg
        tau0 = 0; % delay in seconds
        K = 0;% amplyfying coeffitient in dB
        SSobj; % sound source object
        preasure;
    end
    
    properties (Constant)
        p0 = 2e-5;
    end
    
    methods
        %% constructor
        function obj = Source(SSfile, position, K)
           if nargin < 3
               error('Eneter path to SoundSource object file, position of source and amplification coefficient');
           end
           
           if exist(SSfile, 'file') ~= 2
               error('File with SoundSource object does not exist');
           end
           
           if (length(position) ~= 3) || (~isempty(find(position < 0, 1))) || (~isreal(position))
               error('Position are not a valid values');
           end
           
           if (length(K) ~= 1) || (~isreal(K))
               error('Amplification coefficient is not a valid value');
           end
           
           loadedObj = load(SSfile);
           if ~isa(loadedObj.obj, 'SoundSource')
               error('Selected file is not a SoundSource object');
           end
           
           obj.SSobj = loadedObj.obj;
           obj.position = position;
           obj.K = K;
        end
        
        %% preasure calculation    
        function obj = calcSrcPreasure(obj, grid, freq)
            if isempty(obj.preasure)
                obj.preasure.Abs = zeros([size(grid.X), 32]);
                obj.preasure.Ang = zeros([size(grid.X), 32]);
            end
            
            sizeGrid = size(grid.X);
            A = zeros(sizeGrid);
            Phi = A;
            for xP = 1:size(A,1)
                for yP = 1:size(A,2)
                    [A(xP,yP), Phi(xP,yP)] = calcPreasure([grid.X(xP,yP), grid.Y(xP,yP), 0], obj, freq);
                end
            end
            obj.preasure.Abs(:,:,freq) = A;
            obj.preasure.Ang(:,:,freq) = Phi;
        end
    end
    
end

