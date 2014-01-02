package us.chotchki.springWeb.utility;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;

import us.chotchki.springWeb.service.TimeFormatter;

/**
 * Class inspired by here: https://github.com/tswcode/thymeleaf-joda-dialect/blob/master/src/main/java/de/tswco/thymeleaf/joda/JodaDialect.java
 * @author chotchki
 *
 */
@Service
public class TimeDialect extends AbstractDialect implements IExpressionEnhancingDialect {
	public static final String DEFAULT_PREFIX = "time";
    public static final String JODA_EXPRESSION_OBJECT_NAME = "time";
    
    @Autowired
    private TimeFormatter time;

	@Override
	public String getPrefix() {
		return DEFAULT_PREFIX;
	}

	@Override
	public Map<String, Object> getAdditionalExpressionObjects(IProcessingContext processingContext) {
        final IContext context = processingContext.getContext();
        final IWebContext webContext = (context instanceof IWebContext ? (IWebContext) context : null);
        
        final Map<String, Object> objects = new HashMap<>(1, 1.0f);
        
        if(webContext != null) {
        	objects.put(JODA_EXPRESSION_OBJECT_NAME, time);
        }
        
        return objects;
	}

}
