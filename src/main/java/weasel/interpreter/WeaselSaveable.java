package weasel.interpreter;

import java.io.DataOutputStream;
import java.io.IOException;

public interface WeaselSaveable {

	void saveToDataStream(DataOutputStream dataOutputStream) throws IOException;
	
}
