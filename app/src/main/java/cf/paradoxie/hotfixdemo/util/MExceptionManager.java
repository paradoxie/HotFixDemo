package cf.paradoxie.hotfixdemo.util;

public class MExceptionManager {
	public static void throwApplictionInitEx(String msg){
		throw new MException(msg);
	}
}