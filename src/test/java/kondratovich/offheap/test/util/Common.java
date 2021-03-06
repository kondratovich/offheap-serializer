package kondratovich.offheap.test.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import kondratovich.offheap.core.util.Reflection;
import kondratovich.offheap.core.util.U;
import sun.misc.Unsafe;

public final class Common {
	private Common() {}
	
	public static long time() { 
	  // TODO how to get correct time ?
	  return System.nanoTime();
	}
	
	public static String hex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (byte b : bytes) {
			sb.append(String.format("%1$02X ", b));
			if (i++ % 4 == 3) sb.append("\n");
		}

		return sb.toString().trim();
	}
	
	public static String toString(Object o) throws IllegalArgumentException, IllegalAccessException {
	  StringBuilder sb = new StringBuilder();
	  List<Field> fs = Reflection.getAllFields(o.getClass());
	  for (Field f : fs) {
	    f.setAccessible(true);
	    sb.append("\n").append(Reflection.name0(f, o.getClass())).append(" ").append(f.get(o));
	  }
	  
	  return sb.toString();
	}
	
  public static boolean equal(Object arg0, Object arg1) throws IllegalArgumentException, IllegalAccessException {
    if (arg0 == null && arg1 == null) return true;
    if (arg0 == null || arg1 == null) return false;
    if (arg0.getClass() != arg1.getClass()) return false;
    
    if (arg0.getClass().isPrimitive()) return arg0 == arg1;
    
    List<Field> fs = Reflection.getAllFields(arg0.getClass());
    
    boolean value = true;
    for (Field f : fs) {
      f.setAccessible(true);
      
      if (f.getType().isPrimitive()) {
        if (!f.get(arg0).equals(f.get(arg1)))
          return false;
      } else if (!equal(f.get(arg0), f.get(arg1)))
        return false;
    }
    
    return value;
  }
  
  public static String random(int s) {
    Random r = new Random(System.nanoTime());
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s; i++) sb.append((char) r.nextInt(255));
    
    return sb.toString();
  }
  
  public static boolean array_equal(Object[] arg0, Object[] arg1) throws IllegalArgumentException, IllegalAccessException {
    if (arg0 == null && arg1 == null) return true;
    if (arg0.length != arg1.length) return false;
    
    for (int i = 0; i < arg0.length; i++)
      if (!equal(arg0[i], arg1[i])) return false;
    
    return true;
  }
  
  public static void dump(long ref, int size) {
    byte[] hex = new byte[size];
    for (int i = 0; i < size; i++) hex[i] = u.getByte(ref + i);
    System.out.println(hex(hex));
  }
  
  public static void dump(Object o, int size) {
    byte[] hex = new byte[size];
    for (int i = 0; i < size; i++) hex[i] = u.getByte(o, (long) i);
    System.out.println(hex(hex));
  }
  
  private static final Unsafe u = U.instance();
}