package com.kinlhp.moname.commons.logging;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"test", "profile-group"})
@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest
public abstract class AbstractActiveProfilesIT {
}
