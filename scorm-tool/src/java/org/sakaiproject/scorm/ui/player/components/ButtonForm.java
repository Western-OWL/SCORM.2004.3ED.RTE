/**
 * Copyright (c) 2007 The Apereo Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://opensource.org/licenses/ecl2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sakaiproject.scorm.ui.player.components;

import org.adl.sequencer.SeqNavRequests;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.sakaiproject.scorm.model.api.SessionBean;
import org.sakaiproject.scorm.service.api.ScormSequencingService;
import org.sakaiproject.scorm.ui.player.pages.PlayerPage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;

public class ButtonForm extends Form {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Log LOG = LogFactory.getLog(ButtonForm.class);

	private ActivityAjaxLink prevButton;
	private ActivityAjaxLink nextButton;
	private ActivityAjaxLink startButton;
	private ActivityAjaxLink quitButton;
	private ActivityAjaxLink suspendButton;

	private PlayerPage view;
	
	@SpringBean(name="org.sakaiproject.scorm.service.api.ScormSequencingService")
	ScormSequencingService sequencingService;
	
	public ButtonForm(String id, final SessionBean sessionBean, PlayerPage view) {
		super(id);
		this.view = view;
		
		prevButton = new ActivityAjaxLink(this, sessionBean, "prevButton", SeqNavRequests.NAV_PREVIOUS);
		nextButton = new ActivityAjaxLink(this, sessionBean, "nextButton", SeqNavRequests.NAV_CONTINUE);
		startButton = new ActivityAjaxLink(this, sessionBean, "startButton", SeqNavRequests.NAV_START);
		quitButton = new ActivityAjaxLink(this, sessionBean, "quitButton", SeqNavRequests.NAV_EXITALL);
		suspendButton = new ActivityAjaxLink(this, sessionBean, "suspendButton", SeqNavRequests.NAV_SUSPENDALL);

		add(prevButton);
		add(nextButton);
		add(startButton);
		add(quitButton);
		add(suspendButton);
				
		setOutputMarkupId(true);
		synchronizeState(sessionBean, null);
	}
	
	public void synchronizeState(SessionBean sessionBean, AjaxRequestTarget target) {
		boolean isContinueEnabled = sequencingService.isContinueEnabled(sessionBean);
		boolean isContinueExitEnabled = sequencingService.isContinueExitEnabled(sessionBean);
		boolean isPreviousEnabled = sequencingService.isPreviousEnabled(sessionBean);
		boolean isStartEnabled = sequencingService.isStartEnabled(sessionBean);
		boolean isSuspendEnabled = sequencingService.isSuspendEnabled(sessionBean);
		
		setNextButtonVisible(isContinueEnabled, target);
		setPrevButtonVisible(isPreviousEnabled, target);
		setStartButtonVisible(isStartEnabled, target);
		setSuspendButtonVisible(isSuspendEnabled, target);
		setQuitButtonVisible(isContinueExitEnabled, target);
	}

	public void setPrevButtonVisible(boolean isVisible, AjaxRequestTarget target) {
		setButtonVisible(prevButton, isVisible, target);
	}
	
	public void setNextButtonVisible(boolean isVisible, AjaxRequestTarget target) {
		setButtonVisible(nextButton, isVisible, target);
	}
	
	public void setStartButtonVisible(boolean isVisible, AjaxRequestTarget target) {
		setButtonVisible(startButton, isVisible, target);
	}
	
	public void setQuitButtonVisible(boolean isVisible, AjaxRequestTarget target) {
		setButtonVisible(quitButton, isVisible, target);
	}
	
	public void setSuspendButtonVisible(boolean isVisible, AjaxRequestTarget target) {
		setButtonVisible(suspendButton, isVisible, target);
	}

	private void setButtonVisible(ActivityAjaxLink button, boolean isEnabled, AjaxRequestTarget target) {
		if (null != button) { 
			boolean wasEnabled = button.isEnabled();
			button.setEnabled(isEnabled);
			
			if (target != null)
			{
				target.addComponent(button);
			}
		}
	}
	
	public LaunchPanel getLaunchPanel() {
		return view.getLaunchPanel();
	}

	public ActivityAjaxLink getQuitButton() {
		return quitButton;
	}

}
