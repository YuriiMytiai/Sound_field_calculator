classdef Reciever
    %Source calss with info about position of SoundSource object in current
    %area
    %   Detailed explanation goes here
    
    properties
        position % [x y z] position in m, z=0;
        RP; % recieving pattern
        FR; % frequency response
    end
    
    methods
        %% constructor
        function obj = Source(position)
           if nargin < 1
               error('Eneter position of reciever');
           end
           
           if (length(position) ~= 3) || (~isempty(find(position <= 0, 1))) || (~isreal(position)) || (position(3) ~= 0)
               error('Position are not a valid values');
           end
           
           obj.position = posiiton;
 
        end
    end
    
end

