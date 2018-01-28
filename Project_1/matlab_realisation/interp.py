import scipy.io
import numpy as np
from scipy import interpolate

def python_interp(A):
    X = np.arange(0, 181, 1)
    Y = np.arange(0, 91, 1)
    Xx, Yy = np.meshgrid(X, Y)

    A = np.array(A)
    A = np.reshape(A, (181, 91))
    A = np.transpose(A)
    ok = ~np.isnan(A)
    cartcoord = list(zip(Xx[ok].ravel(), Yy[ok].ravel()))
    f = interpolate.LinearNDInterpolator(cartcoord, A[ok].ravel())
    Z = f(Xx, Yy)
    return Z