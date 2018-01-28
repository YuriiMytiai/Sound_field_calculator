function mat2h5(filename)
if exist(filename, 'file') ~= 2
    error('File doesn"t exist');
end
matFile = load(filename);

hdf5write([filename(1:end-3), 'h5'], '/sens', matFile.sensitivity);
hdf5write([filename(1:end-3), 'h5'], '/imped', matFile.impedance, 'WriteMode', 'append');
hdf5write([filename(1:end-3), 'h5'], '/sizes', matFile.sizes, 'WriteMode', 'append');
hdf5write([filename(1:end-3), 'h5'], '/weight', matFile.weight, 'WriteMode', 'append');

    for i = 1:(32)
        ampRP = matFile.amplitudeRP(:,:,i); 
        phsRP = matFile.phaseRP(:,:,i); 
    
        hdf5write([filename(1:end-3), 'h5'], ['/amplRP', num2str(i)], ampRP, 'WriteMode', 'append');
        hdf5write([filename(1:end-3), 'h5'], ['/phaseRP', num2str(i)], phsRP, 'WriteMode', 'append');
    end
end