package redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
@Data
public class run {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public String seqGenerator(String key) {
        //加上时间戳 如果不需要
        String datetime = new SimpleDateFormat("yyMMdd").format(new Date());
        //查询 key 是否存在， 不存在返回 1 ，存在的话则自增加1
        Long autoID = redisTemplate.opsForValue().increment(key + datetime, 1);
        //这里是 4 位id，如果位数不够可以自行修改 ，下面的意思是 得到上面 key 的 值，位数为 4 ，不够	的话在左边补 0 ，比如  110 会变成  0110
        String value = StringUtils.leftPad(String.valueOf(autoID), 4, "0");
        //然后把 时间戳和优化后的 ID 拼接
        String code = MessageFormat.format("{0}{1}", key + datetime, value);
        //设置三天过期
        redisTemplate.expire(key + datetime, 3, TimeUnit.DAYS);
        return code;
    }

}
