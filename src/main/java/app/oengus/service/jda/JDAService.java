package app.oengus.service.jda;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.utils.MiscUtil;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.requests.CompletedRestAction;
import net.dv8tion.jda.internal.requests.Route;
import net.dv8tion.jda.internal.requests.restaction.MessageActionImpl;
import net.dv8tion.jda.internal.utils.config.AuthorizationConfig;
import net.dv8tion.jda.internal.utils.config.MetaConfig;
import net.dv8tion.jda.internal.utils.config.SessionConfig;
import net.dv8tion.jda.internal.utils.config.ThreadingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@Service
public class JDAService {
    private final JDAImpl jda;

    @Autowired
    public JDAService(
        @Value("${discord.botTokenRaw}") final String botToken
    ) {
        final AuthorizationConfig authConfig = new AuthorizationConfig(botToken);
        final SessionConfig sessionConfig = SessionConfig.getDefault();
        final ThreadingConfig threadConfig = ThreadingConfig.getDefault();
        final MetaConfig metaConfig = MetaConfig.getDefault();

        threadConfig.setRateLimitPool(Executors.newScheduledThreadPool(5, (r) -> {
            final Thread t = new Thread(r, "JDA-rest-thread");
            t.setDaemon(true);
            return t;
        }), true);

        this.jda = new JDAImpl(authConfig, sessionConfig, threadConfig, metaConfig);
    }

    public RestAction<Message> sendMessage(final String channelId, final MessageEmbed embed) {
        try {
            MiscUtil.parseSnowflake(channelId);
        } catch (final NumberFormatException ignored) {
            return new CompletedRestAction<>(this.jda, (Message) null);
        }

        final Route.CompiledRoute route = Route.Messages.SEND_MESSAGE.compile(channelId);

        return new CustomMessageActionImpl(this.jda, route).embed(embed);
    }
}