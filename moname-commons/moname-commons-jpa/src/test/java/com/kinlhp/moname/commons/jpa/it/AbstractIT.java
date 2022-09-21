package com.kinlhp.moname.commons.jpa.it;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.kinlhp.moname.commons.jpa.TestConfiguration;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(classes = { TestConfiguration.class })
@DataJpaTest
abstract class AbstractIT {
}
