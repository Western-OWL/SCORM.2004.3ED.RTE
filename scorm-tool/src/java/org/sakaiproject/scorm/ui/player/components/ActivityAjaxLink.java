package org.sakaiproject.scorm.ui.player.components;

import org.adl.sequencer.SeqNavRequests;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.sakaiproject.scorm.model.api.SessionBean;
import org.sakaiproject.scorm.service.api.LearningManagementSystem;
import org.sakaiproject.scorm.service.api.ScormResourceService;
import org.sakaiproject.scorm.service.api.ScormSequencingService;
import org.sakaiproject.scorm.ui.ResourceNavigator;
import org.sakaiproject.scorm.ui.player.util.Utils;

public class ActivityAjaxLink extends AjaxFallbackLink
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Log LOG = LogFactory.getLog(ActivityAjaxLink.class);

	private ButtonForm form;

	private int seqRequest;

	@SpringBean(name="org.sakaiproject.scorm.service.api.ScormResourceService")
	ScormResourceService resourceService;
	@SpringBean(name="org.sakaiproject.scorm.service.api.ScormSequencingService")
	ScormSequencingService sequencingService;

	public ActivityAjaxLink(final ButtonForm form , SessionBean sessionBean, String id, int seqRequest)
	{
		super(id);
		this.form = form;
		this.seqRequest = seqRequest;
		this.setModel(new Model(sessionBean));
	}

	private void doNavigate(SessionBean sessionBean, int seqRequest, AjaxRequestTarget target)
	{
		sequencingService.navigate(seqRequest, sessionBean, new LocalResourceNavigator(), target);

		
		if (form.getLaunchPanel() != null)
		{
			form.getLaunchPanel().synchronizeState(sessionBean, target);
			form.getLaunchPanel().getTree().selectNode();
			form.getLaunchPanel().getTree().updateTree(target);
		}
	}

	public void onClick(AjaxRequestTarget target)
	{
		SessionBean sessionBean = (SessionBean)getDefaultModelObject();
		modelChanging();
		doNavigate(sessionBean, seqRequest, target);
		modelChanging();
	}

	public class LocalResourceNavigator extends ResourceNavigator
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected ScormResourceService resourceService()
		{
			return ActivityAjaxLink.this.resourceService;
		}

		@Override
		public Component getFrameComponent()
		{
			if (form.getLaunchPanel() != null && form.getLaunchPanel().getContentPanel() != null)
			{
				return form.getLaunchPanel().getContentPanel();
			}
			return null;
		}
	}
}
