package brain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DumbPasswordEncoderTest {

    //unit-test
    @Test
    public void encode() {
        DumbPasswordEncoder encoder = new DumbPasswordEncoder();
        Assert.assertEquals("secret: pwd", encoder.encode("pwd"));
        MatcherAssert.assertThat(encoder.encode("mypwd"), Matchers.containsString("mypwd"));
    }
}