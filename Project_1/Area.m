classdef Area < handle
    %Area is class with properies oflisteners' square
    %   Detailed explanation goes here
    
    properties
        xSize
        ySize
        xStep
        yStep
        grid
        Sources = cell(1,50);
        Recievers = cell(1,50);
    end
    
    methods
        %% object constructor
        function obj = Area(xSize, ySize, xStep, yStep)
            if nargin < 3
                error('You need to enter at least X and Y sizes of area and grid step');
            end
            if nargin < 4
                yStep = xStep;
            end
            
            obj.xSize = xSize;
            obj.ySize = ySize;
            obj.xStep = xStep;
            obj.yStep = yStep;
            
            numXpoints = xSize / xStep;
            if mod(numXpoints, 1) ~= 0
                numXpoints = numXpoints + 1;
                obj.xStep = obj.xSize / numXpoints;
            end
            
            numYpoints = ySize / yStep;
            if mod(numYpoints, 1) ~= 0
                numYpoints = numYpoints + 1;
                obj.yStep = obj.ySize / numYpoints;
            end
            
            xPoints = linspace(0, obj.xSize, numXpoints);
            yPoints = linspace(0, obj.ySize, numYpoints);
            obj.grid = xPoints' * yPoints;
        end
        
        %% add Sources method
        function obj = addSource(obj, source)
            if (nargin < 2) || (~isa(source, 'Source'))
                eroor('Enter name of Source object');
            end
            if source.position(1) > obj.xSize
                error('X position of source is out of boundaries. Source position must be inside of area');
            end
            if source.position(2) > obj.ySize
                error('Y position of source is out of boundaries. Source position must be inside of area');
            end
            
            emptyCells = find(cellfun('isempty', obj.Sources), 1, 'first');
            obj.Sources{1, emptyCells} = source;
        end
        
        %% add Recievers method
        function obj = addRecievers(obj, reciever)
            if (nargin < 2) || (~isa(reciever, 'Reciever'))
                eroor('Enter name of Reciever object');
            end
            if reciever.position(1) > obj.xSize
                error('X position of reciever is out of boundaries. Reciever position must be inside of area');
            end
            if reciever.position(2) > obj.ySize
                error('Y position of reciever is out of boundaries. Reciever position must be inside of area');
            end
            
            emptyCells = find(cellfun('isempty', obj.Recievers), 1, 'first');
            obj.Recievers{1, emptyCells} = source;
        end
    end
    
end

