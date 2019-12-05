package com.oax.service;

import java.util.List;

import com.oax.entity.front.InvitingFeedback;

public interface InvitingFeedbackService {
    List<InvitingFeedback> pullInvitingFeedbackList(String beginTime, String endTime);
    boolean save(InvitingFeedback invitingFeedback);
}
