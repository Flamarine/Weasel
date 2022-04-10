package weasel.interpreter;

public interface WeaselNativeMethod {

	Object invoke(WeaselClassBuffer interpreter, String methodName, Object _this, Object[] param);
	
}
