package us.henge.utility.security;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

import us.henge.utility.security.processors.ProtectDataRootFolder;
import us.henge.utility.security.processors.ProtectHrefProcessor;
import us.henge.utility.security.processors.ProtectSrcProcessor;
import us.henge.utility.security.processors.ProtectValueProcessor;

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
        processors.add(new ProtectDataRootFolder());
        processors.add(new ProtectSrcProcessor());
        processors.add(new ProtectValueProcessor());
        return processors;
    }
}
