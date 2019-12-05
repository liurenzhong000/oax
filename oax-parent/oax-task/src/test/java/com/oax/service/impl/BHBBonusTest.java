package com.oax.service.impl;

import com.oax.OaxApiApplicationTest;
import com.oax.scheduled.NewBonusScheduled;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BHBBonusTest extends OaxApiApplicationTest {

    @Autowired
    private NewBonusScheduled newBonusScheduled;

    @Test
    public void test(){
        newBonusScheduled.BHBBonus();
    }
}
