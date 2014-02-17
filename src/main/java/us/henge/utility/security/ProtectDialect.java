package us.henge.utility.security;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

public class ProtectDialect extends AbstractDialect {
	public ProtectDialect() {
		super();
	}

	@Override
	public String getPrefix() {
		return "protect";
	}

    @Override
    public Set<IProcessor> getProcessors() {
        final Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new ProtectHrefProcessor());
        return processors;
    }
}
