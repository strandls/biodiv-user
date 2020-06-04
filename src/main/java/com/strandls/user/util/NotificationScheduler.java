package com.strandls.user.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strandls.user.RabbitMqConnection;
import com.rabbitmq.client.Channel;
import com.strandls.mail_utility.model.EnumModel.NOTIFICATION_DATA;
import com.strandls.mail_utility.model.EnumModel.NOTIFICATION_FIELDS;
import com.strandls.mail_utility.producer.RabbitMQProducer;
import com.strandls.mail_utility.util.JsonUtil;
import com.strandls.user.pojo.FirebaseTokens;

public class NotificationScheduler extends Thread {

	Channel channel;
	String title;
	String body;
	List<FirebaseTokens> tokens;

	public NotificationScheduler(Channel channel, String title, String body, List<FirebaseTokens> tokens) {
		this.channel = channel;
		this.title = title;
		this.body = body;
		this.tokens = tokens;
	}

	@Override
	public void run() {
		try {
			if (this.tokens != null && this.tokens.size() > 0) {
				RabbitMQProducer producer = new RabbitMQProducer(channel);
				for (FirebaseTokens token : tokens) {
					Map<String, Object> data = new HashMap<String, Object>();
					data.put(NOTIFICATION_FIELDS.TO.getAction(), token.getToken());
					Map<String, Object> notification = new HashMap<String, Object>();
					notification.put(NOTIFICATION_DATA.TITLE.getAction(), title);
					notification.put(NOTIFICATION_DATA.BODY.getAction(), body);
					data.put(NOTIFICATION_FIELDS.NOTIFICATION.getAction(), JsonUtil.unflattenJSON(notification));
					producer.produceNotification(RabbitMqConnection.EXCHANGE,
							RabbitMqConnection.NOTIFICATION_ROUTING_KEY, null, JsonUtil.mapToJSON(data));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
