package com.ben.test;

import com.ben.Car;
import com.ben.bytebuddy.Bar;
import com.ben.bytebuddy.Foo;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class playTest {

    @Test
    void spy0(){

        List list = new LinkedList();
        List spy = spy(list);

        //optionally, you can stub out some methods:
        when(spy.size()).thenReturn(100);

        //using the spy calls real methods
        spy.add("one");
        spy.add("two");

        //prints "one" - the first element of a list
        System.out.println(spy.get(0));

        //size() method was stubbed - 100 is printed
        System.out.println(spy.size());

        //optionally, you can verify
        verify(spy).add("one");
        verify(spy).add("two");
    }

    @Test
    void mockitoStub(){
        LinkedList mockedList = mock(LinkedList.class);

        // stubbing appears before the actual execution
        when(mockedList.get(0)).thenReturn("first");

        // the following prints "first"
        assertThat(mockedList.get(0)).isEqualTo("first");
        assertThat(mockedList.get(1)).isNull();
    }

    @Test
    void mockitoMock(){
        // mock creation
        List mockedList = mock(List.class);

        mockedList.add("one");
        mockedList.clear();

        verify(mockedList).add("one");
        verify(mockedList).clear();
    }

    @Test
    void testCarMake(){
        Car car1 = new Car("A1");
        CarAssert carAssert = new CarAssert(car1);
        carAssert.hasMake("A1");
    }


    @DisplayName("ByteBuddy hello world")
    @Test
    void testSingleSuccessTest() throws InstantiationException, IllegalAccessException {

        DynamicType.Unloaded<Object> unloadedType = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.isToString())
                .intercept(FixedValue.value("Hello World ByteBuddy!"))
                .make();


        Class<?> dynamicType = unloadedType.load(getClass().getClassLoader()).getLoaded();

        String res = dynamicType.newInstance().toString();

        assertThat(res).isEqualTo("Hello World ByteBuddy!");
    }

    @DisplayName("ByteBuddy hello world2")
    @Test
    void test2() throws InstantiationException, IllegalAccessException {

        String r = new ByteBuddy()
                .subclass(Foo.class)
                .method(named("sayHelloFoo")
                        .and(isDeclaredBy(Foo.class)
                                .and(returns(String.class))))
                .intercept(MethodDelegation.to(Bar.class))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance()
                .sayHelloFoo();



        assertThat(r).isEqualTo(Bar.sayHelloBar());
    }

//    @Test
//    void t3(){
//        ByteBuddyAgent.install();
//        new ByteBuddy()
//                .redefine(Foo.class)
//                .method(named("sayHelloFoo"))
//                .intercept(FixedValue.value("Hello Foo Redefined"))
//                .make()
//                .load(
//                        Foo.class.getClassLoader(),
//                        ClassReloadingStrategy.fromInstalledAgent());
//
//        Foo f = new Foo();
//
//        assertEquals(f.sayHelloFoo(), "Hello Foo Redefined");
//    }










//    @BeforeAll
//    static void setup() {
//        System.out.println("setup up");
//    }
//
//    @BeforeEach
//    void init() {
//        System.out.println("setup up");
//    }

}
