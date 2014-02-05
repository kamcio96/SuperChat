package pl.kastir.SuperChat.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Super useful class made by CaptainBern and slightly modified by stirante
 * 
 * @author CaptainBern
 * 
 * @param <T>
 */
public class SafeField<T> {
    
    private Field   field;
    private boolean isStatic;
    
    public SafeField(Field field) {
        setField(field);
    }
    
    public SafeField(Class<?> coreClass, String fieldName) {
        try {
            Field field = coreClass.getDeclaredField(fieldName);
            setField(field);
        }
        catch (NoSuchFieldException e) {
            System.out.println("Failed to find a matching field with name: " + fieldName);
        }
    }
    
    protected void setField(Field field) {
        if (!field.isAccessible()) {
            try {
                field.setAccessible(true);
            }
            catch (Exception e) {
                e.printStackTrace();
                field = null;
            }
        }
        this.field = field;
        this.isStatic = Modifier.isStatic(field.getModifiers());
    }
    
    public boolean set(Object instance, T value) {
        if (!isStatic && instance == null) { throw new UnsupportedOperationException("Non-static fields require a valid instance passed in!"); }
        
        try {
            this.field.set(instance, value);
            return true;
        }
        catch (IllegalAccessException e) {
            System.out.println("Failed to access field: " + toString());
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    public T get(Object instance) {
        if (!isStatic && instance == null) { throw new UnsupportedOperationException("Non-static fields require a valid instance passed in!"); }
        try {
            return (T) this.field.get(instance);
        }
        catch (IllegalAccessException e) {
            System.out.println("Failed to access field: " + toString());
        }
        return null;
    }
    
    public T transfer(Object from, Object to) {
        if (this.field == null) { return null; }
        T old = get(to);
        set(to, get(from));
        return old;
    }
    
    public String getName() {
        return this.field.getName();
    }
    
    public String toString() {
        StringBuilder string = new StringBuilder(75);
        int mod = this.field.getModifiers();
        if (Modifier.isPublic(mod)) {
            string.append("public ");
        }
        else if (Modifier.isPrivate(mod)) {
            string.append("private ");
        }
        else if (Modifier.isProtected(mod)) {
            string.append("protected ");
        }
        
        if (Modifier.isStatic(mod)) {
            string.append("static ");
        }
        
        string.append(this.field.getName());
        
        return string.toString();
    }
    
    public boolean isPublic() {
        return Modifier.isPublic(field.getModifiers());
    }
    
    public boolean isReadOnly() {
        return Modifier.isFinal(field.getModifiers());
    }
    
    public SafeField<T> setReadOnly(boolean value) {
        if (value) set(field, "modifiers", field.getModifiers() | Modifier.FINAL);
        else set(field, "modifiers", field.getModifiers() & ~Modifier.FINAL);
        return this;
    }
    
    public static <T> T get(Class<?> clazz, String fieldname) {
        return new SafeField<T>(clazz, fieldname).get(null);
    }
    
    public static <T> T get(Object instance, String fieldName) {
        return new SafeField<T>(instance.getClass(), fieldName).get(instance);
    }
    
    public static <T> T get(Class<?> clazz, Object instance, String fieldName) {
        return new SafeField<T>(clazz, fieldName).get(instance);
    }
    
    public static <T> T get(Class<?> clazz, Object instance, String fieldName, Class<T> castClass) {
        return (T) (new SafeField<T>(clazz, fieldName).get(instance));
    }
    
    public static <T> void set(Object instance, String fieldName, T value) {
        new SafeField<T>(instance.getClass(), fieldName).set(instance, value);
    }
    
    public static <T> void set(Class<?> clazz, Object instance, String fieldName, T value) {
        new SafeField<T>(clazz, fieldName).setReadOnly(false).set(instance, value);
    }
    
    public static <T> void setStatic(Class<?> clazz, String fieldname, T value) {
        new SafeField<T>(clazz, fieldname).set(null, value);
    }
}