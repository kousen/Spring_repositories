package com.kousenit.entities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "fullname", types = { Officer.class })
public interface FullNameProjection {
    @Value("#{target.rank} #{target.first} #{target.last}")
    String getFullName();
}
