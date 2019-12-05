/**
 *
 */

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.oax.common.RedisUtil;

/**
 * @author ：xiangwh
 * @ClassName:：UserInfoTest
 * @Description： TODO
 * @date ：2018年6月4日 上午9:26:52
 */
public class UserInfoTest {
    RestTemplate restTemplate;

    @Before
    public void init() {
        restTemplate = new RestTemplate();
    }

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void getUserInfoList() {
//        List<Banner> banners = new ArrayList<>();
//        Banner banner = new Banner();
//        banner.setImage("www.oax.com");
//        banner.setSortNo(5);
//        banners.add(banner);
//        redisUtil.setList("xxx", banners);
//
//        System.out.println();

        Instant now = Instant.now();


        System.out.println(now);


        /*String url = "http://localhost:8080/userInfo/property";
         *//*UserCoin info = new UserCoin();
		User user = new User();
		user.setId(2);
		info.setUser(user);*//*
		String result = restTemplate.postForObject(url, 2, String.class);
		System.out.println(result);*/

		/*String s = "EOS/BTC";
		int i = s.lastIndexOf("/");

		System.out.println(s);

		System.out.println(i+1);
		//截取前半截
		String substring = s.substring(0, (i));
		//截取后半截
		String subs = s.substring(i + 1);
		System.out.println(substring);
		System.out.println(subs);*/

    }
}





















