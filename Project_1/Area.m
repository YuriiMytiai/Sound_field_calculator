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
            if nargin < 2
                eroor('Enter name of source object');
            end
            
            emptyCells = find(cellfun('isempty', obj.Sources), 1, 'first');
            obj.Sources{1, emptyCells} = source;
        end
        
        %% add Recievers method
        function obj = addRecievers(obj, source)
            if nargin < 2
                eroor('Enter name of reciever object');
            end
            
            emptyCells = find(cellfun('isempty', obj.Recievers), 1, 'first');
            obj.Recievers{1, emptyCells} = source;
        end
    end
    
end

