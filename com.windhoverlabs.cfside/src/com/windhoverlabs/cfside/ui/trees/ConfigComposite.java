package com.windhoverlabs.cfside.ui.trees;

import java.io.File;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.windhoverlabs.cfside.ui.composites.ConfigTableComposite;
import com.windhoverlabs.cfside.ui.tables.KeyValueTable;
import com.windhoverlabs.cfside.utils.JsonObjectsUtil;

public class ConfigComposite extends Composite {
	
	Composite configTreeHolder;
	Tree tree;
	TreeViewer treeViewer;
	TreeColumn objectLabel;
	TreeColumn objectChildrenCount;
	
	Composite tableKeyValueHolder;
	ConfigTableComposite tableKeyValueViewer;
	KeyValueTable keyValueTable;
	
	String currentFilePath;
	String currentFileName;
	
	JsonParser jsonParser;
	
	public ConfigComposite(Composite parent, int style, String currentFile) {
		super(parent, style);
		this.currentFilePath = currentFile;
		createObjectViewer();
		createTableKeyValueViewer();
		
		treeViewer.refresh();
	}
	
	private void createObjectViewer() {
		jsonParser = new JsonParser();
		setLayout(new FillLayout(SWT.HORIZONTAL));
		tree = new Tree(this, SWT.BORDER);
		tree.setHeaderVisible(true);
		
		objectLabel = new TreeColumn(tree, SWT.NONE);
		objectLabel.setText("Current file : " + this.currentFileName);
		objectLabel.setWidth(300);
		
		objectChildrenCount = new TreeColumn(tree, SWT.NONE);
		objectChildrenCount.setText("Count");
		objectChildrenCount.setWidth(100);
			
		/**
		Reader rd = null;
		try {
			rd = new FileReader(new File(currentFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		JsonElement jsonElement = jsonParser.parse(rd);
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		
		**/
		JsonElement jsonElement = JsonObjectsUtil.goMerge(new File("/home/vagrant/development/airliner/config/bebop2/config.json"));
		treeViewer = new TreeViewer(this.tree);
		treeViewer.setContentProvider(new JsonContentProvider());
		treeViewer.setLabelProvider(new JsonLabelProvider());
		treeViewer.setInput(new JsonObjectTreeNode(jsonElement, "root"));
		
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				TreeViewer viewer = (TreeViewer) event.getViewer();
				IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
				Object selectedNode = thisSelection.getFirstElement();
				if (selectedNode instanceof JsonObjectTreeNode) {
					JsonObjectTreeNode jtn = (JsonObjectTreeNode) selectedNode;
					JsonElement newElement = jtn.getJsonElement();
					keyValueTable.setNewConfig(newElement);
				} else {
					JsonValueTreeNode jtn = (JsonValueTreeNode) selectedNode;
					JsonElement newElement = jtn.getJsonElement();
					keyValueTable.setNewConfig(newElement);
				}
			}
		});
	
	}
	
	private void createTableKeyValueViewer() {
		String testJson = "{\n" + 
				"      \"definition\": \"../../apps/sch/fsw/for_build/definition.json\",\n" + 
				"      \"commands\": {\n" + 
				"        \"SCH_CMD_MID\": {\n" + 
				"          \"msgid\": \"0x123d\",\n" + 
				"          \"commands\": {\n" + 
				"            \"Noop\": {\n" + 
				"              \"cc\": \"0\",\n" + 
				"              \"struct\": \"CFE_SB_CmdHdr_t\"\n" + 
				"            },\n" + 
				"            \"Reset\": {\n" + 
				"              \"cc\": \"1\",\n" + 
				"              \"struct\": \"CFE_SB_CmdHdr_t\"\n" + 
				"            },\n" + 
				"            \"Enable\": {\n" + 
				"              \"cc\": \"2\",\n" + 
				"              \"struct\": \"SCH_EntryCmd_t\"\n" + 
				"            },\n" + 
				"            \"Disable\": {\n" + 
				"              \"cc\": \"3\",\n" + 
				"              \"struct\": \"SCH_EntryCmd_t\"\n" + 
				"            },\n" + 
				"            \"EnableGroup\": {\n" + 
				"              \"cc\": \"4\",\n" + 
				"              \"struct\": \"SCH_GroupCmd_t\"\n" + 
				"            },\n" + 
				"            \"DisableGroup\": {\n" + 
				"              \"cc\": \"5\",\n" + 
				"              \"struct\": \"SCH_GroupCmd_t\"\n" + 
				"            },\n" + 
				"            \"EnableSync\": {\n" + 
				"              \"cc\": \"6\",\n" + 
				"              \"struct\": \"CFE_SB_CmdHdr_t\"\n" + 
				"            },\n" + 
				"            \"SendDiag\": {\n" + 
				"              \"cc\": \"7\",\n" + 
				"              \"struct\": \"CFE_SB_CmdHdr_t\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"SCH_SEND_HK_MID\": {\n" + 
				"          \"msgid\": \"0x19e5\",\n" + 
				"          \"commands\": {\n" + 
				"            \"SendHK\": {\n" + 
				"              \"cc\": \"0\",\n" + 
				"              \"struct\": \"CFE_SB_CmdHdr_t\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"SCH_UNUSED_MID\": {\n" + 
				"          \"msgid\": \"0\",\n" + 
				"          \"commands\": {\n" + 
				"            \"Unused\": {\n" + 
				"              \"cc\": \"0\",\n" + 
				"              \"struct\": \"CFE_SB_CmdHdr_t\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        }\n" + 
				"      },\n" + 
				"      \"telemetry\": {\n" + 
				"        \"SCH_ACTIVITY_DONE_MID\": {\n" + 
				"          \"msgid\": \"0x1238\",\n" + 
				"          \"struct\": \"CFE_SB_TlmHdr_t\"\n" + 
				"        },\n" + 
				"        \"SCH_HK_TLM_MID\": {\n" + 
				"          \"msgid\": \"0x09e8\",\n" + 
				"          \"struct\": \"SCH_HkPacket_t\"\n" + 
				"        },\n" + 
				"        \"SCH_DIAG_TLM_MID\": {\n" + 
				"          \"msgid\": \"0x09e7\",\n" + 
				"          \"struct\": \"SCH_DiagPacket_t\"\n" + 
				"        }\n" + 
				"      },\n" + 
				"      \"short_name\": \"SCH\",\n" + 
				"      \"long_name\": \"Scheduler\",\n" + 
				"      \"code_templates\": {\n" + 
				"        \"msgids\": {\n" + 
				"          \"template\": \"../platform_inc/sch_msgids.j2\",\n" + 
				"          \"output\": \"sch_msgids.h\"\n" + 
				"        },\n" + 
				"        \"perfids\": {\n" + 
				"          \"template\": \"../mission_inc/sch_perfids.j2\",\n" + 
				"          \"output\": \"sch_perfids.h\"\n" + 
				"        },\n" + 
				"        \"platform_cfg\": {\n" + 
				"          \"template\": \"../platform_inc/sch_platform_cfg.j2\",\n" + 
				"          \"output\": \"sch_platform_cfg.h\"\n" + 
				"        }\n" + 
				"      },\n" + 
				"      \"events\": {\n" + 
				"        \"SCH_INITSTATS_INF_EID\": {\n" + 
				"          \"id\": \"1\"\n" + 
				"        },\n" + 
				"        \"SCH_APP_EXIT_EID\": {\n" + 
				"          \"id\": \"2\"\n" + 
				"        },\n" + 
				"        \"SCH_CR_PIPE_ERR_EID\": {\n" + 
				"          \"id\": \"3\"\n" + 
				"        },\n" + 
				"        \"SCH_SUB_HK_REQ_ERR_EID\": {\n" + 
				"          \"id\": \"4\"\n" + 
				"        },\n" + 
				"        \"SCH_SUB_GND_CMD_ERR_EID\": {\n" + 
				"          \"id\": \"5\"\n" + 
				"        },\n" + 
				"        \"SCH_SDT_REG_ERR_EID\": {\n" + 
				"          \"id\": \"7\"\n" + 
				"        },\n" + 
				"        \"SCH_MDT_REG_ERR_EID\": {\n" + 
				"          \"id\": \"8\"\n" + 
				"        },\n" + 
				"        \"SCH_SDT_LOAD_ERR\": {\n" + 
				"          \"id\": \"9\"\n" + 
				"        },\n" + 
				"        \"SCH_MDT_LOAD_ERR\": {\n" + 
				"          \"id\": \"10\"\n" + 
				"        },\n" + 
				"        \"SCH_ACQ_PTR_ERR\": {\n" + 
				"          \"id\": \"11\"\n" + 
				"        },\n" + 
				"        \"SCH_MINOR_FRAME_TIMER_CREATE_ERR\": {\n" + 
				"          \"id\": \"12\"\n" + 
				"        },\n" + 
				"        \"SCH_MINOR_FRAME_TIMER_ACC_WARN\": {\n" + 
				"          \"id\": \"13\"\n" + 
				"        },\n" + 
				"        \"SCH_MAJOR_FRAME_SUB_ERR\": {\n" + 
				"          \"id\": \"14\"\n" + 
				"        },\n" + 
				"        \"SCH_SEM_CREATE_ERR\": {\n" + 
				"          \"id\": \"15\"\n" + 
				"        },\n" + 
				"        \"SCH_SAME_SLOT\": {\n" + 
				"          \"id\": \"16\"\n" + 
				"        },\n" + 
				"        \"SCH_SKIPPED_SLOTS\": {\n" + 
				"          \"id\": \"17\"\n" + 
				"        },\n" + 
				"        \"SCH_MULTI_SLOTS\": {\n" + 
				"          \"id\": \"18\"\n" + 
				"        },\n" + 
				"        \"SCH_CORRUPTION\": {\n" + 
				"          \"id\": \"19\"\n" + 
				"        },\n" + 
				"        \"SCH_PACKET_SEND\": {\n" + 
				"          \"id\": \"20\"\n" + 
				"        },\n" + 
				"        \"SCH_NOISY_MAJOR_FRAME_ERR\": {\n" + 
				"          \"id\": \"21\"\n" + 
				"        },\n" + 
				"        \"SCH_SCHEDULE_TBL_ERR\": {\n" + 
				"          \"id\": \"30\"\n" + 
				"        },\n" + 
				"        \"SCH_SCHEDULE_TABLE\": {\n" + 
				"          \"id\": \"31\"\n" + 
				"        },\n" + 
				"        \"SCH_MESSAGE_TBL_ERR\": {\n" + 
				"          \"id\": \"32\"\n" + 
				"        },\n" + 
				"        \"SCH_MESSAGE_TABLE\": {\n" + 
				"          \"id\": \"33\"\n" + 
				"        },\n" + 
				"        \"SCH_NOOP_CMD\": {\n" + 
				"          \"id\": \"40\"\n" + 
				"        },\n" + 
				"        \"SCH_RESET_CMD\": {\n" + 
				"          \"id\": \"41\"\n" + 
				"        },\n" + 
				"        \"SCH_ENABLE_CMD\": {\n" + 
				"          \"id\": \"42\"\n" + 
				"        },\n" + 
				"        \"SCH_DISABLE_CMD\": {\n" + 
				"          \"id\": \"43\"\n" + 
				"        },\n" + 
				"        \"SCH_ENA_GRP_CMD\": {\n" + 
				"          \"id\": \"44\"\n" + 
				"        },\n" + 
				"        \"SCH_DIS_GRP_CMD\": {\n" + 
				"          \"id\": \"45\"\n" + 
				"        },\n" + 
				"        \"SCH_ENA_SYNC_CMD\": {\n" + 
				"          \"id\": \"46\"\n" + 
				"        },\n" + 
				"        \"SCH_SEND_DIAG_CMD\": {\n" + 
				"          \"id\": \"47\"\n" + 
				"        },\n" + 
				"        \"SCH_ENABLE_CMD_ARG_ERR\": {\n" + 
				"          \"id\": \"50\"\n" + 
				"        },\n" + 
				"        \"SCH_ENABLE_CMD_ENTRY_ERR\": {\n" + 
				"          \"id\": \"51\"\n" + 
				"        },\n" + 
				"        \"SCH_DISABLE_CMD_ARG_ERR\": {\n" + 
				"          \"id\": \"52\"\n" + 
				"        },\n" + 
				"        \"SCH_DISABLE_CMD_ENTRY_ERR\": {\n" + 
				"          \"id\": \"53\"\n" + 
				"        },\n" + 
				"        \"SCH_ENA_GRP_CMD_ERR\": {\n" + 
				"          \"id\": \"54\"\n" + 
				"        },\n" + 
				"        \"SCH_ENA_GRP_NOT_FOUND_ERR\": {\n" + 
				"          \"id\": \"55\"\n" + 
				"        },\n" + 
				"        \"SCH_DIS_GRP_CMD_ERR\": {\n" + 
				"          \"id\": \"56\"\n" + 
				"        },\n" + 
				"        \"SCH_DIS_GRP_NOT_FOUND_ERR\": {\n" + 
				"          \"id\": \"57\"\n" + 
				"        },\n" + 
				"        \"SCH_CC_ERR\": {\n" + 
				"          \"id\": \"58\"\n" + 
				"        },\n" + 
				"        \"SCH_MD_ERR\": {\n" + 
				"          \"id\": \"59\"\n" + 
				"        },\n" + 
				"        \"SCH_CMD_LEN_ERR\": {\n" + 
				"          \"id\": \"60\"\n" + 
				"        },\n" + 
				"        \"SCH_DEADLINE_REG_ERR_EID\": {\n" + 
				"          \"id\": \"61\"\n" + 
				"        },\n" + 
				"        \"SCH_SLOT_DEADLINE_FULL_ERR_EID\": {\n" + 
				"          \"id\": \"62\"\n" + 
				"        },\n" + 
				"        \"SCH_SUB_ACTIVITY_DONE_REQ_ERR_EID\": {\n" + 
				"          \"id\": \"63\"\n" + 
				"        },\n" + 
				"        \"SCH_UNEXPECTED_ACT_DONE_ERR_EID\": {\n" + 
				"          \"id\": \"64\"\n" + 
				"        },\n" + 
				"        \"SCH_MUTEX_CREATE_ERR_EID\": {\n" + 
				"          \"id\": \"65\"\n" + 
				"        },\n" + 
				"        \"SCH_AD_CHILD_TASK_CREATE_ERR\": {\n" + 
				"          \"id\": \"66\"\n" + 
				"        },\n" + 
				"        \"SCH_AD_RCVD_UNEXPECTED_MSG_ERR_EID\": {\n" + 
				"          \"id\": \"67\"\n" + 
				"        }\n" + 
				"      },\n" + 
				"      \"perfids\": {\n" + 
				"        \"SCH_APPMAIN_PERF_ID\": {\n" + 
				"          \"value\": \"61\"\n" + 
				"        }\n" + 
				"      },\n" + 
				"      \"tables\": {\n" + 
				"        \"MSG_DEFS\": {\n" + 
				"          \"title\": \"Message Definitions\",\n" + 
				"          \"isDumpTable\": \"false\",\n" + 
				"          \"default\": \"sch_def_msgtbl.tbl\",\n" + 
				"          \"template\": \"../tables/sch_def_msgtbl.j2\",\n" + 
				"          \"_config_wizard\": {\n" + 
				"            \"class\": \"com.windhoverlabs.airliner.apps.sch.UISchMsgDefTableWizard\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"SCHED_DEF\": {\n" + 
				"          \"title\": \"Schedule Definitions\",\n" + 
				"          \"isDumpTable\": \"false\",\n" + 
				"          \"default\": \"sch_def_schtbl.tbl\",\n" + 
				"          \"template\": \"../tables/sch_def_schtbl.j2\",\n" + 
				"          \"_config_wizard\": {\n" + 
				"            \"class\": \"com.windhoverlabs.airliner.apps.sch.UISchSchDefTableWizard\"\n" + 
				"          }\n" + 
				"        }\n" + 
				"      },\n" + 
				"      \"config\": {\n" + 
				"        \"SCH_PIPE_DEPTH\": {\n" + 
				"          \"title\": \"Software Bus Command Pipe Depth\",\n" + 
				"          \"description\": \"Dictates the number of messages to SCH that can be queued while awaiting processing by the SCH Application.\",\n" + 
				"          \"limits\": \"Must be greater than zero.\",\n" + 
				"          \"value\": \"2\"\n" + 
				"        },\n" + 
				"        \"SCH_TOTAL_SLOTS\": {\n" + 
				"          \"title\": \"Minor Frame Frequency (in Hz)\",\n" + 
				"          \"description\": \"Dictates the number of minor frame slots within each Major Frame.\",\n" + 
				"          \"limits\": \"Must be 2 or more and less than 65536\",\n" + 
				"          \"value\": \"250\"\n" + 
				"        },\n" + 
				"        \"SCH_ENTRIES_PER_SLOT\": {\n" + 
				"          \"title\": \"Maximum number of Activities per Minor Frame\",\n" + 
				"          \"description\": \"Dictates the number of activities that can be defined for each Minor Frame.\",\n" + 
				"          \"limits\": \"Must be at least one.\",\n" + 
				"          \"value\": \"15\"\n" + 
				"        },\n" + 
				"        \"SCH_MAX_MESSAGES\": {\n" + 
				"          \"title\": \"Maximum Number of Message Definitions in Message Definition Table\",\n" + 
				"          \"description\": \"Dictates the number of messages that can be defined in Message Definition Table.\",\n" + 
				"          \"limits\": \"Must be at least one.\",\n" + 
				"          \"value\": \"128\"\n" + 
				"        },\n" + 
				"        \"SCH_MDT_MIN_MSG_ID\": {\n" + 
				"          \"title\": \"Minimum Message ID allowed in Message Definition Table\",\n" + 
				"          \"description\": \"Dictates the minimum message ID that can be used in the Message Definition Table.\",\n" + 
				"          \"limits\": \"Must be less than or equal to SCH_MDT_MAX_MSG_ID and greater than or equal to 0.\",\n" + 
				"          \"value\": \"0\"\n" + 
				"        },\n" + 
				"        \"SCH_MDT_MAX_MSG_ID\": {\n" + 
				"          \"title\": \"Maximum Message ID allowed in Message Definition Table\",\n" + 
				"          \"description\": \"Dictates the maximum message ID that can be used in the Message Definition Table.\",\n" + 
				"          \"limits\": \"Must be less than or equal to #CFE_SB_HIGHEST_VALID_MSGID and greater than SCH_MDT_MIN_MSG_ID.\",\n" + 
				"          \"value\": \"CFE_SB_HIGHEST_VALID_MSGID\"\n" + 
				"        },\n" + 
				"        \"SCH_MAX_MSG_WORDS\": {\n" + 
				"          \"title\": \"Maximum Length, in Words, of a Message\",\n" + 
				"          \"description\": \"Dictates the maximum number of words that can be assigned to a particular message in the Message Definition Table.\",\n" + 
				"          \"limits\": \"Must be at least large enough to hold the smallest possible message header (see #CFE_SB_TLM_HDR_SIZE and #CFE_SB_CMD_HDR_SIZE).\",\n" + 
				"          \"value\": \"64\"\n" + 
				"        },\n" + 
				"        \"SCH_MAX_LAG_COUNT\": {\n" + 
				"          \"title\": \"Maximum Number of slots allowed for catch-up before skipping\",\n" + 
				"          \"description\": \"Dictates the number of Minor Frames that will be processed in \\\"Catch Up\\\" mode before giving up and skipping ahead.\",\n" + 
				"          \"limits\": \"\",\n" + 
				"          \"value\": \"(SCH_TOTAL_SLOTS / 2)\"\n" + 
				"        },\n" + 
				"        \"SCH_MAX_SLOTS_PER_WAKEUP\": {\n" + 
				"          \"title\": \"Maximum Number of Slots to be processed when in \\\"Catch Up\\\" mode\",\n" + 
				"          \"description\": \"Dictates the maximum number of slots SCH will process when trying to \\\"Catch Up\\\" to the correct slot for the current time.\",\n" + 
				"          \"limits\": \"Must be at least one.\",\n" + 
				"          \"value\": \"5\"\n" + 
				"        },\n" + 
				"        \"SCH_MICROS_PER_MAJOR_FRAME\": {\n" + 
				"          \"title\": \"Major Frame Period (in microseconds)\",\n" + 
				"          \"description\": \"Dictates the number microseconds in a Major Frame.\",\n" + 
				"          \"limits\": \"Must be greater than zero.\",\n" + 
				"          \"value\": \"1000000\"\n" + 
				"        },\n" + 
				"        \"SCH_SYNC_SLOT_DRIFT_WINDOW\": {\n" + 
				"          \"title\": \"Additional time allowed in Sync Slot to wait for Major Frame Sync (in microseconds)\",\n" + 
				"          \"description\": \"Dictates the additional time allowed in the Syncronization Slot to allow the Major Frame Sync signal to be received and re-synchronize processing.\",\n" + 
				"          \"limits\": \"Must be less than the normal slot period.\",\n" + 
				"          \"value\": \"500\"\n" + 
				"        },\n" + 
				"        \"SCH_STARTUP_SYNC_TIMEOUT\": {\n" + 
				"          \"title\": \"Time, in milliseconds, to wait for all applications to be started and ready to run\",\n" + 
				"          \"description\": \"Dictates the timeout for the #CFE_ES_WaitForStartupSync call that SCH uses to wait for all of the Applications specified in the startup script to finish initialization.  SCH will wait this amount of time before assuming all startup script applications have been started and will then begin nominal schedule processing.\",\n" + 
				"          \"limits\": \"None\",\n" + 
				"          \"value\": \"50000\"\n" + 
				"        },\n" + 
				"        \"SCH_STARTUP_PERIOD\": {\n" + 
				"          \"title\": \"Time, in microseconds, to wait for first Major Frame Sync to arrive\",\n" + 
				"          \"description\": \"Dictates the time allowed for the first Major Frame sync signal to arrive before assuming it is not going to occur and switching to a freewheeling mode.\",\n" + 
				"          \"limits\": \"Must be greater than or equal to the Major Frame Period.\",\n" + 
				"          \"value\": \"(5*SCH_MICROS_PER_MAJOR_FRAME)\"\n" + 
				"        },\n" + 
				"        \"SCH_MAX_NOISY_MAJORF\": {\n" + 
				"          \"title\": \"Maximum Number of consecutive Noisy Major Frame signals before they are ignored\",\n" + 
				"          \"description\": \"Dictates the number of consecutive \\\"Noisy\\\" Major Frame Signals (i.e. - signals that occur outside the expected window of their occurence) until the Major Frame signal is automatically ignored and the Minor Frame Timer is used instead.\",\n" + 
				"          \"limits\": \"This value should never be set to less than two because a single \\\"noisy\\\" Major Frame signal is likely when turning on or switching the 1 Hz signal on the spacecraft.\",\n" + 
				"          \"value\": \"2\"\n" + 
				"        },\n" + 
				"        \"SCH_LIB_PRESENCE\": {\n" + 
				"          \"title\": \"Scheduler API Library Usage Status\",\n" + 
				"          \"description\": \"Determines whether or not the Scheduler application is using the API library allowing for external tasks to disable and enable schedule processing. Note that if the library is to be used it must be generated as a separate object, and loaded prior to the appplication.\",\n" + 
				"          \"limits\": \"This value must either be 0 when not using or including the library, or 1 if the library is going to be used.\",\n" + 
				"          \"value\": \"1\"\n" + 
				"        },\n" + 
				"        \"SCH_LIB_DIS_CTR\": {\n" + 
				"          \"title\": \"Scheduler API Library Initial Inhibition Count\",\n" + 
				"          \"description\": \"Sets the number of times the scheduler must be enabled following library initialization. This allows the scheduler to come up either enabled (set to 0) or disabled (set to a value greater than or equal to 1)\",\n" + 
				"          \"limits\": \"This value must be an unsigned 32 bit integer.\",\n" + 
				"          \"value\": \"0\"\n" + 
				"        },\n" + 
				"        \"SCH_SCHEDULE_FILENAME\": {\n" + 
				"          \"title\": \"Default SCH Schedule Definition Table Filename\",\n" + 
				"          \"description\": \"The value of this constant defines the default filename of the SCH Schedule Definition Table\",\n" + 
				"          \"limits\": \"The length of each string, including the NULL terminator cannot exceed the #OS_MAX_PATH_LEN value.\",\n" + 
				"          \"value\": \"\\\"/cf/apps/sch_def_schtbl.tbl\\\"\"\n" + 
				"        },\n" + 
				"        \"SCH_MESSAGE_FILENAME\": {\n" + 
				"          \"title\": \"Default SCH Message Definition Table Filename\",\n" + 
				"          \"description\": \"The value of this constant defines the default filename of the SCH Message Definition Table\",\n" + 
				"          \"limits\": \"The length of each string, including the NULL terminator cannot exceed the #OS_MAX_PATH_LEN value.\",\n" + 
				"          \"value\": \"\\\"/cf/apps/sch_def_msgtbl.tbl\\\"\"\n" + 
				"        },\n" + 
				"        \"SCH_MISSION_REV\": {\n" + 
				"          \"title\": \"Mission specific version number for SCH application\",\n" + 
				"          \"description\": \"An application version number consists of four parts: major version number, minor version number, revision number and mission specific revision number. The mission specific revision number is defined here and the other parts are defined in \\\"sch_version.h\\\".\",\n" + 
				"          \"limits\": \"Must be defined as a numeric value that is greater than or equal to zero.\",\n" + 
				"          \"value\": \"1\"\n" + 
				"        },\n" + 
				"        \"SCH_AD_PIPE_DEPTH\": {\n" + 
				"          \"title\": \"\",\n" + 
				"          \"description\": \"\",\n" + 
				"          \"limits\": \"\",\n" + 
				"          \"value\": \"3\"\n" + 
				"        },\n" + 
				"        \"SCH_AD_CHILD_TASK_PRIORITY\": {\n" + 
				"          \"title\": \"\",\n" + 
				"          \"description\": \"\",\n" + 
				"          \"limits\": \"\",\n" + 
				"          \"value\": \"10\"\n" + 
				"        },\n" + 
				"        \"SCH_DEADLINES_PER_SLOT\": {\n" + 
				"          \"title\": \"\",\n" + 
				"          \"description\": \"\",\n" + 
				"          \"limits\": \"\",\n" + 
				"          \"value\": \"5\"\n" + 
				"        },\n" + 
				"        \"SCH_AD_CHILD_TASK_FLAGS\": {\n" + 
				"          \"title\": \"\",\n" + 
				"          \"description\": \"\",\n" + 
				"          \"limits\": \"\",\n" + 
				"          \"value\": \"OS_ENABLE_CORE_0\"\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }";
		JsonElement je = jsonParser.parse(testJson);
	
		keyValueTable = new KeyValueTable(this, SWT.FILL, je);
		
		System.out.println(je.toString());
	}

	
	public void refreshViewer() {
		treeViewer.refresh();
	}
	
	
}
