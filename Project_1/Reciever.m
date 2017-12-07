classdef Reciever < handle
    %Source calss with info about position of SoundSource object in current
    %area
    %   Detailed explanation goes here
    
    properties
        position % [x y z] position in m, z=0;
        RP; % recieving pattern
        MFR = zeros(1, 32); % magnitude frequency response
        PFR = zeros(1, 32) % phase frequenct response
    end
    
    properties (Constant)
        p0 = 2e-5;
    end
    
    methods
        %% constructor
        function obj = Reciever(position)
           if nargin < 1
               error('Eneter position of reciever');
           end
           
           if (length(position) ~= 3) || (~isempty(find(position < 0, 1))) || (~isreal(position))
               error('Position are not a valid values');
           end
           
           obj.position = position;
 
        end
        
        %% frequency response calculation
        function obj = calcFR(obj, Sources)
            NumSources  = numel((find(~cellfun('isempty', Sources))));
            notEmptyCells = (find(~cellfun('isempty', Sources)));
            p = zeros(1, 32);
            
            for freq = 1:32 % for all frequencies
                for curS = 1:NumSources % for all sources
                    [A, Phi] = calcPreasure(obj.position, Sources{notEmptyCells(curS)}, freq);
                    p(1,freq) = p(1,freq) + obj.p0*10^(A/20) * exp(-1i*Phi); % add all sorces' preasures at one frequency
                end
            end
            obj.MFR = 20.*log10(abs(p)./(obj.p0));
            obj.PFR = angle(p);
        end
    end
    
end

