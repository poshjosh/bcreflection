package com.bc.reflection.function;

import java.util.List;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * @author hp
 */
public class FindClassesInPackageTest {
    
    public FindClassesInPackageTest() { }

    /**
     * Test of apply method, of class FindClassesInPackage.
     */
    @Test
    public void testApply() {
        System.out.println("apply");
        
        String packageName = this.getClass().getPackage().getName();
        FindClassesInPackage instance = new FindClassesInPackage();
        List<Class<?>> result = instance.apply(packageName);
        assertThat(result, notNullValue());
        assertThat(result.size(), not(0));
//        System.out.println(result.stream().map(Object::toString).collect(Collectors.joining("\n")));
    }
}
