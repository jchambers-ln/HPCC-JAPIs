package org.hpccsystems.ws.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.axis.client.Stub;
import org.hpccsystems.ws.client.HPCCWsSMCClient;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ApplicationValue;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ArrayOfEspException;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.DebugValue;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ECLException;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ECLGraph;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ECLHelpFile;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ECLWorkunit;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ECLQuery;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ECLResult;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ECLSchemaItem;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ECLSourceFile;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ECLTimer;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ECLTimingData;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.EspException;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.QuerySet;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUAbort;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUAction;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ECLWUActions;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ECLWorkflow;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUActionResponse;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUCreateAndUpdate;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUDelete;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUInfo;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUInfoDetails;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUInfoResponse;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUPublishWorkunit;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUPublishWorkunitResponse;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUQuery;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUQueryResponse;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUQuerySetDetailsResponse;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUQuerysetDetails;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUQuerysets;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUQuerysetsResponse;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUResubmit;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUResult;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUResultResponse;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WURun;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WURunResponse;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUSubmit;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUSubmitResponse;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUSyntaxCheckECL;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUSyntaxCheckResponse;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WUUpdateResponse;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WsWorkunitsServiceSoap;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WsWorkunitsServiceSoapProxy;
import org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ThorLogInfo;
import org.hpccsystems.ws.client.platform.WUState;
import org.hpccsystems.ws.client.platform.WorkunitInfo;
import org.hpccsystems.ws.client.platform.Version;
import org.hpccsystems.ws.client.utils.Connection;
import org.hpccsystems.ws.client.utils.DataSingleton;
import org.hpccsystems.ws.client.utils.EqualsUtil;
import org.hpccsystems.ws.client.utils.HashCodeUtil;
import org.hpccsystems.ws.client.utils.Utils;

/**
 *
 * ESP Client code for common WsWorkUnits operations
 */
public class HPCCWsWorkUnitsClient extends DataSingleton
{
    public static HPCCWsWorkUnitsClient get(Connection connection)
    {
        return new HPCCWsWorkUnitsClient(connection);
    }

    public static final String                                                          WSWORKUNITSWSDLURI                = "/WsWorkunits";
    private WsWorkunitsServiceSoapProxy                                                 wsWorkunitsServiceSoapProxy       = null;
    private org.hpccsystems.ws.client.gen.wsworkunits.v1_56.WsWorkunitsServiceSoapProxy fallBackWorkunitsServiceSoapProxy = null;
    public static final int                                                             defaultWaitTime                   = 10000;
    public static final int                                                             defaultResultLimit                = 100;
    public static final int                                                             defaultMaxWaitTime                = 1000
            * 60 * 5;
    private Version                                                                     targetVersion                     = null;
    private boolean                                                                     verbose                           = false;

    private static URL                                                                  originalURL;

    public static URL getOriginalURL() throws MalformedURLException
    {
        if (originalURL == null) originalURL = new URL(getOriginalWSDLURL());

        return originalURL;
    }

    public static int getOriginalPort() throws MalformedURLException
    {
        return getOriginalURL().getPort();
    }

    protected void fastWURefresh(WorkunitInfo wu) throws Exception
    {
        getSoapProxy();

        WUState previousState = getStateID(wu);

        WUQuery request = new WUQuery();
        request.setWuid(wu.getWuid());
        request.setCount(1);

        WUQueryResponse response = wsWorkunitsServiceSoapProxy.WUQuery(request);
        if (response.getWorkunits() != null && response.getWorkunits().length == 1)
        {
            wu.update(response.getWorkunits()[0]);
        }

        if (previousState != getStateID(wu))
        {
            fullWURefresh(wu);
        }
    }

    protected void fullWURefresh(WorkunitInfo wu) throws Exception
    {
        fullWURefresh(wu, true, true, true, true);
    }

    void fullWURefresh(WorkunitInfo wu, boolean includeGraphs, boolean includeResults, boolean includeSourceFiles,
            boolean includeApplicationValues) throws Exception
    {
        getSoapProxy();

        WUInfo request = new WUInfo();
        request.setWuid(wu.getWuid());
        request.setIncludeGraphs(includeGraphs);
        request.setIncludeResults(includeResults);
        request.setIncludeResultsViewNames(includeResults);
        request.setSuppressResultSchemas(!includeResults);
        request.setIncludeSourceFiles(includeSourceFiles);
        request.setIncludeApplicationValues(includeApplicationValues);

        WUInfoResponse response = wsWorkunitsServiceSoapProxy.WUInfo(request);
        if (response.getWorkunit() == null)
        {
            // Call succeeded, but no response...
            for (EspException e : response.getExceptions().getException())
            {
                if (e.getCode().equals("20082") || e.getCode().equals("20080"))
                {
                    // No longer exists... //$NON-NLS-1$ //$NON-NLS-2$
                    wu.setStateID(999);
                    // setChanged();
                    // notifyObservers(Notification.WORKUNIT);
                    break;
                }
            }
        }
        else
        {
            wu.update(response.getWorkunit());
        }
    }

    /**
     * @param verbose
     *            - sets verbose mode
     */
    public void setVerbose(boolean verbose)
    {
        this.verbose = verbose;
    }

    /**
     * Provides soapproxy object for HPCCWsWorkUnitsClient which can be used to access the web service methods directly
     *
     * @return soapproxy for HPCCWsWorkUnitsClient
     * @throws Exception
     *             if soapproxy not available
     */
    public WsWorkunitsServiceSoapProxy getSoapProxy() throws Exception
    {
        if (wsWorkunitsServiceSoapProxy != null)
            return wsWorkunitsServiceSoapProxy;
        else
            throw new Exception("wsTopologyServiceSoapProxy not available.");
    }

    /**
     * Provides the WSDL URL originally used to create the underlying stub code
     *
     * @return original WSLD URL
     */
    public static String getOriginalWSDLURL()
    {
        return (new org.hpccsystems.ws.client.gen.wsworkunits.v1_62.WsWorkunitsLocator())
                .getWsWorkunitsServiceSoapAddress();
    }

    protected HPCCWsWorkUnitsClient(WsWorkunitsServiceSoapProxy wsWorkunitsServiceSoapProxy)
    {
        this.wsWorkunitsServiceSoapProxy = wsWorkunitsServiceSoapProxy;
    }

    protected HPCCWsWorkUnitsClient(Connection baseConnection)
    {
        this(baseConnection.getProtocol(), baseConnection.getHost(), baseConnection.getPort(),
                baseConnection.getUserName(), baseConnection.getPassword());
    }

    protected HPCCWsWorkUnitsClient(String protocol, String targetHost, String targetPort, String user, String pass)
    {
        String address = Connection.buildUrl(protocol, targetHost, targetPort, WSWORKUNITSWSDLURI);
        HPCCWsSMCClient wssmc = new HPCCWsSMCClient(protocol, targetHost, targetPort, user, pass);
        targetVersion = new Version(wssmc.getHPCCBuild());
        targetVersion.postfix = "";
        targetVersion.prefix = "";
        initWsWorkUnitsSoapProxy(address, user, pass);
    }

    /**
     * Initializes the service's underlying soap proxy. Should only be used by constructors
     *
     * @param baseURL
     *            Target service base URL
     * @param user
     *            User credentials
     * @param pass
     *            User credentials
     */
    private void initWsWorkUnitsSoapProxy(String baseURL, String user, String pass)
    {
        wsWorkunitsServiceSoapProxy = new WsWorkunitsServiceSoapProxy(baseURL);
        if (wsWorkunitsServiceSoapProxy != null)
        {
            WsWorkunitsServiceSoap wsWorkunitsServiceSoap = wsWorkunitsServiceSoapProxy.getWsWorkunitsServiceSoap();
            if (user != null && pass != null) Connection.initStub((Stub) wsWorkunitsServiceSoap, user, pass);
        }

        fallBackWorkunitsServiceSoapProxy = new org.hpccsystems.ws.client.gen.wsworkunits.v1_56.WsWorkunitsServiceSoapProxy(
                baseURL);
        if (fallBackWorkunitsServiceSoapProxy != null)
        {
            org.hpccsystems.ws.client.gen.wsworkunits.v1_56.WsWorkunitsServiceSoap fallBackWorkunitsServiceSoap = fallBackWorkunitsServiceSoapProxy
                    .getWsWorkunitsServiceSoap();
            if (user != null && pass != null) Connection.initStub((Stub) fallBackWorkunitsServiceSoap, user, pass);
        }
    }

    /**
     * Reports if the WU in question is in the compiled state. Makes a call to the target Web Service to extract
     * information about the WU
     *
     * @param wuid
     * @return true if state is compiled
     * @throws Exception
     */
    public boolean isWorkunitCompiled(String wuid) throws Exception
    {
        WorkunitInfo thewui = getWUInfo(wuid);
        if (thewui != null) return isWorkunitCompiled(thewui);

        return false;
    }

    /**
     * Reports if the WU in question is in the compiled state. Does not make call to the target Web Service, extracts
     * information from the WU object
     *
     * @param wuid
     * @return true if state is compiled
     */
    public boolean isWorkunitCompiled(WorkunitInfo thewui)
    {
        return isWorkunitState(getStateID(thewui), WUState.COMPILED);
    }

    /**
     * Reports if the WU in question is in the failed state. Does not make call to the target Web Service, extracts
     * information from the WU object
     *
     * @param wuid
     * @return true if state is failed
     */
    public boolean isWorkunitFailed(WorkunitInfo thewui)
    {
        return isWorkunitState(getStateID(thewui), WUState.FAILED);
    }

    /**
     * Reports if the string state represents the failed state
     *
     * @param state
     * @return true if state is failed
     */
    public boolean isWorkunitFailed(String state)
    {
        return state.equalsIgnoreCase(WUState.FAILED.toString());
    }

    /**
     * Compares two WuStates
     *
     * @param state1
     * @param state2
     * @return true if state1 == state2
     */
    private boolean isWorkunitState(WUState state1, WUState state2)
    {
        return state1 == state2;
    }

    /**
     * Replies true if given wuid is in complete state. This method makes call to WS, it is preferable to use WU object
     * version
     *
     * @param wuid
     * @return
     * @throws Exception
     */
    public boolean isWorkunitComplete(String wuid) throws Exception
    {
        WorkunitInfo thewui = getWUInfo(wuid);
        if (thewui != null) return isWorkunitComplete(thewui);

        return false;
    }

    /**
     * Replies true if given wuid is in complete state. This method does not makea call to WS, and it is preferable over
     * the version that calls WS.
     *
     * @param wuid
     * @return true if wu is in one of the complete states
     * @throws Exception
     */
    static public boolean isWorkunitComplete(WorkunitInfo thewui)
    {
        return isWorkunitComplete(getStateID(thewui));
    }

    /**
     * reports if WuState is in the completed set
     *
     * @param state
     * @return true if state is in completed set
     */
    static public boolean isWorkunitComplete(WUState state)
    {
        switch (state)
        {
            case UNKNOWN_ONSERVER:
            case COMPLETED:
            case FAILED:
            case ABORTED:
            case ARCHIVED:
            case COMPILED:
                return true;
            case ABORTING:
            case BLOCKED:
            case COMPILING:
            case LAST:
            case RUNNING:
            case SCHEDULED:
            case SUBMITTED:
            case UNKNOWN:
            case WAIT:
            default:
                return false;
        }
    }

    /**
     *
     * @param wu
     *            the workunit
     * @return the state of the workunit
     */
    public static WUState getStateID(WorkunitInfo wu)
    {
        if (wu != null)
            return getStateID(wu.getStateID());
        else
            return WUState.UNKNOWN;
    }

    /**
     * Converts integer representation of WU state, to WUState enumeration
     *
     * @param id
     * @return WU state enumeration
     */
    public static WUState getStateID(Integer id)
    {
        switch (id)
        {
            case 1:
                return WUState.COMPILED;
            case 2:
                return WUState.RUNNING;
            case 3:
                return WUState.COMPLETED;
            case 4:
                return WUState.FAILED;
            case 5:
                return WUState.ARCHIVED;
            case 6:
                return WUState.ABORTING;
            case 7:
                return WUState.ABORTED;
            case 8:
                return WUState.BLOCKED;
            case 9:
                return WUState.SUBMITTED;
            case 10:
                return WUState.SCHEDULED;
            case 11:
                return WUState.COMPILING;
            case 12:
                return WUState.WAIT;
            case 13:
                return WUState.WAIT;
            case 14:
                return WUState.WAIT;
            case 15:
                return WUState.RUNNING;
            case 999:
                return WUState.UNKNOWN_ONSERVER;
            default:
                return WUState.UNKNOWN;
        }
    }

    /**
     * Attempts to create, compile and publish a query based on ecl provided.
     *
     * @param ecl
     * @param jobname
     * @param targetcluster
     * @param waitMillis
     * @param resultLimit
     * @return
     * @throws Exception
     */
    public WUPublishWorkunitResponse publishWUFromEcl(WorkunitInfo wu) throws Exception
    {
        WUPublishWorkunitResponse publishWUResp = null;

        if (wsWorkunitsServiceSoapProxy == null)
            throw new Exception("wsWorkunitsServiceSoapProxy not available");
        else if (wu.getECL() == null || wu.getECL().length() == 0)
            throw new Exception("Empty ECL submited");
        else
        {
            WorkunitInfo eclwu = createWUFromECL(wu);

            try
            {
                Thread.sleep(wu.getSleepMillis());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            submitWU(eclwu); // if no exception proceed
            monitorWUToCompletion(eclwu);
            publishWUResp = publishWU(eclwu);
        }

        return publishWUResp;
    }

    /**
     * Attempts to publish a query based on a given Workunit.
     *
     * @param wu
     * @param jobname
     * @param targetcluster
     * @return
     * @throws Exception
     */
    public WUPublishWorkunitResponse publishWU(WorkunitInfo wu) throws Exception
    {
        WUPublishWorkunitResponse publishWUResp = null;

        if (wsWorkunitsServiceSoapProxy == null)
            throw new Exception("wsWorkunitsServiceSoapProxy not available");
        else if (wu == null)
            throw new Exception("Invalid wuid submited");
        else
        {
            WUPublishWorkunit publishWU = new WUPublishWorkunit();
            publishWU.setJobName(wu.getJobname());
            publishWU.setWuid(wu.getWuid());
            publishWU.setCluster(wu.getCluster());
            publishWU.setActivate((int) 1);

            publishWUResp = wsWorkunitsServiceSoapProxy.WUPublishWorkunit(publishWU);

            org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ArrayOfEspException exceptions = publishWUResp
                    .getExceptions();
            if (exceptions != null)
            {
                throwWsWUExceptions(exceptions, "Could not publish WU");
            }
        }

        return publishWUResp;
    }

    public void publishWU(String wuid) throws Exception
    {
        if (wsWorkunitsServiceSoapProxy == null)
            throw new Exception("wsWorkunitsServiceSoapProxy not available");
        else if (wuid == null || wuid.isEmpty())
            throw new Exception("Invalid wuid submited");
        else
        {
            WUPublishWorkunit publishWU = new WUPublishWorkunit();
            publishWU.setWuid(wuid);

            wsWorkunitsServiceSoapProxy.WUPublishWorkunit(publishWU);
        }
    }

    /**
     * Get information about a given WorkUnit
     *
     * @param wuid
     *            - ID of target workunit
     * @return - ECLWorkunit object with information pertaining to the WU
     * @throws Exception
     *             - Caller must handle exceptions
     */
    public WorkunitInfo getWUInfo(String wuid) throws Exception
    {
        return getWUInfo(wuid, false);
    }

    /**
     * Get information about a given WorkUnit
     *
     * @param wuid
     *            - ID of target workunit
     * @param unarchive
     *            - unarchive archived workunit
     * @return - ECLWorkunit object with information pertaining to the WU
     * @throws Exception
     *             - Caller must handle exceptions
     */
    public WorkunitInfo getWUInfo(String wuid, boolean unarchive) throws Exception
    {

        WorkunitInfo workunit = null;

        if (wsWorkunitsServiceSoapProxy == null)
            throw new Exception("wsWorkunitsServiceSoapProxy not available");
        else
        {
            WUInfoDetails wuinfodetailsparams = new WUInfoDetails();
            wuinfodetailsparams.setIncludeApplicationValues(false);
            wuinfodetailsparams.setIncludeDebugValues(false);
            wuinfodetailsparams.setIncludeExceptions(true);
            wuinfodetailsparams.setIncludeGraphs(false);
            wuinfodetailsparams.setIncludeResults(true);
            wuinfodetailsparams.setWuid(wuid);

            WUInfoResponse wuInfoResponse = wsWorkunitsServiceSoapProxy.WUInfoDetails(wuinfodetailsparams);

            org.hpccsystems.ws.client.gen.wsworkunits.v1_62.ArrayOfEspException exceptions = wuInfoResponse
                    .getExceptions();
            if (exceptions == null && wuInfoResponse.getWorkunit() != null)
            {
                if (unarchive && wuInfoResponse.getWorkunit().getArchived())
                {
                    doWorkunitAction(wuid, ECLWUActions.Restore);
                    return getWUInfo(wuid);
                }
                workunit = new WorkunitInfo(wuInfoResponse.getWorkunit());
                workunit.setOriginalEclWatchUrl(getEclWatchUrl());

            }
            else
            {
                throwWsWUExceptions(exceptions, "Could not fetch WU Info");
            }
        }

        return workunit;
    }

    public WUInfoResponse getWUInfo(String wuid, boolean includeResults, boolean includeGraphs,
            boolean includeSourceFiles, boolean includeApplicationValues, Boolean includeDebugValues,
            Boolean includeExceptions, Boolean includeVariables, Boolean includeXmlSchemas, Boolean includeTimers)
                    throws Exception
    {
        return getWUInfo(wuid, includeResults, includeGraphs, includeSourceFiles, includeApplicationValues,
                includeDebugValues, includeExceptions, includeVariables, includeXmlSchemas, includeTimers, false);
    }

    public WUInfoResponse getWUInfo(String wuid, boolean includeResults, boolean includeGraphs,
            boolean includeSourceFiles, boolean includeApplicationValues, Boolean includeDebugValues,
            Boolean includeExceptions, Boolean includeVariables, Boolean includeXmlSchemas, Boolean includeTimers,
            boolean unarchive) throws Exception
    {
        if (wsWorkunitsServiceSoapProxy == null)
            throw new Exception("wsWorkunitsServiceSoapProxy not available");
        else
        {
            WUInfo request = new WUInfo();
            request.setWuid(wuid);
            request.setIncludeGraphs(includeGraphs);
            request.setIncludeResults(includeResults);
            request.setIncludeResultsViewNames(includeResults);
            request.setSuppressResultSchemas(!includeResults);
            request.setIncludeSourceFiles(includeSourceFiles);
            request.setIncludeApplicationValues(includeApplicationValues);
            request.setIncludeDebugValues(includeDebugValues);
            request.setIncludeExceptions(includeExceptions);
            request.setIncludeTimers(includeTimers);
            request.setIncludeVariables(includeVariables);
            request.setIncludeXmlSchemas(includeXmlSchemas);

            WUInfoResponse resp = wsWorkunitsServiceSoapProxy.WUInfo(request);
            ECLWorkunit wk = resp.getWorkunit();
            if (unarchive && wk != null && wk.getArchived() != null && wk.getArchived())
            {
                doWorkunitAction(wuid, ECLWUActions.Restore);
                return getWUInfo(wuid, includeResults, includeGraphs, includeSourceFiles, includeApplicationValues,
                        includeDebugValues, includeExceptions, includeVariables, includeXmlSchemas, includeTimers,
                        false);
            }
            return resp;
        }
    }

    /**
     * This method queries WU information using a bogus WUID. Used as a mechanism for testing connectivity with HPCC
     * ESP. Temporarily increases timeout value to 3 seconds;
     *
     * @return - true if able to reply is received within timout value of 3 secs, false otherwise
     * @throws Exception
     *             - Caller must handle exceptions
     */
    public boolean testWUQuery() throws Exception
    {
        int oldTimeout = -1;
        if (wsWorkunitsServiceSoapProxy == null)
            throw new Exception("wsWorkunitsServiceSoapProxy not available");
        else
        {
            WUQuery request = new WUQuery();
            request.setWuid("XXX"); //$NON-NLS-1$
            try
            {
                oldTimeout = ((Stub) wsWorkunitsServiceSoapProxy.getWsWorkunitsServiceSoap()).getTimeout();

                ((org.apache.axis.client.Stub) wsWorkunitsServiceSoapProxy.getWsWorkunitsServiceSoap())
                        .setTimeout(3 * 1000);

                wsWorkunitsServiceSoapProxy.WUQuery(request);
                return true;
            }
            catch (ArrayOfEspException e)
            {}
            catch (RemoteException e)
            {}
            finally
            {
                if (oldTimeout != -1)
                    ((org.apache.axis.client.Stub) wsWorkunitsServiceSoapProxy.getWsWorkunitsServiceSoap())
                            .setTimeout(oldTimeout);
            }
            return false;
        }
    }

    /**
     * Converts EspException from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.EspException
     *            in
     * @return EspException
     */
    private EspException convertEspException(org.hpccsystems.ws.client.gen.wsworkunits.v1_56.EspException in)
    {
        EspException out = new EspException();
        if (in.getCode() != null) out.setCode(in.getCode());
        if (in.getAudience() != null) out.setAudience(in.getAudience());
        if (in.getSource() != null) out.setSource(in.getSource());
        if (in.getMessage() != null) out.setMessage(in.getMessage());
        return out;
    }

    /**
     * Converts ArrayOfEspException from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ArrayOfEspException
     *            in
     * @return ArrayOfEspException
     */
    private ArrayOfEspException convertArrayOfEspException(
            org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ArrayOfEspException in)
    {
        ArrayOfEspException out = new ArrayOfEspException();
        if (in.getSource() != null) out.setSource(in.getSource());
        if (in.getException() != null)
        {
            EspException[] temp_exceptions = new EspException[in.getException().length];
            for (int i = 0; i < in.getException().length; i++)
            {
                temp_exceptions[i] = convertEspException(in.getException(i));
            }
            out.setException(temp_exceptions);
        }
        return out;
    }

    /**
     * Converts ThorLogInfo from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ThorLogInfo
     *            in
     * @return ThorLogInfo
     */
    private ThorLogInfo convertThorLogInfo(org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ThorLogInfo in)
    {
        ThorLogInfo out = new ThorLogInfo();
        if (in.getProcessName() != null) out.setProcessName(in.getProcessName());
        if (in.getClusterGroup() != null) out.setClusterGroup(in.getClusterGroup());
        if (in.getLogDate() != null) out.setLogDate(in.getLogDate());
        if (in.getNumberSlaves() != null) out.setNumberSlaves(in.getNumberSlaves());
        return out;
    }

    /**
     * Converts ECLQuery from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLQuery
     *            in
     * @return ECLQuery
     */
    private ECLQuery convertECLQuery(org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLQuery in)
    {
        ECLQuery out = new ECLQuery();
        if (in.getText() != null) out.setText(in.getText());
        if (in.getCpp() != null) out.setCpp(in.getCpp());
        if (in.getResTxt() != null) out.setResTxt(in.getResTxt());
        if (in.getDll() != null) out.setDll(in.getDll());
        if (in.getDll() != null) out.setThorLog(in.getThorLog());
        if (in.getQueryMainDefinition() != null) out.setQueryMainDefinition(in.getQueryMainDefinition());
        return out;
    }

    /**
     * Converts ECLHelpFile from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLHelpFile
     *            in
     * @return ECLHelpFile
     */
    private ECLHelpFile convertECLHelpFile(org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLHelpFile in)
    {
        ECLHelpFile out = new ECLHelpFile();
        if (in.getName() != null) out.setName(in.getName());
        if (in.getType() != null) out.setType(in.getType());
        if (in.getIPAddress() != null) out.setIPAddress(in.getIPAddress());
        if (in.getDescription() != null) out.setDescription(in.getDescription());
        if (in.getFileSize() != null) out.setFileSize(in.getFileSize());
        if (in.getPID() != null) out.setPID(in.getPID());
        // minActivityID and maxActivityId not supported in 1.56
        return out;
    }

    /**
     * Converts ECLException from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLException
     *            in
     * @return ECLException
     */
    private ECLException convertECLException(org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLException in)
    {
        ECLException out = new ECLException();
        if (in.getSource() != null) out.setSource(in.getSource());
        if (in.getSeverity() != null) out.setSeverity(in.getSeverity());
        if (in.getCode() != null) out.setCode(in.getCode());
        if (in.getMessage() != null) out.setMessage(in.getMessage());
        if (in.getFileName() != null) out.setFileName(in.getFileName());
        if (in.getLineNo() != null) out.setLineNo(in.getLineNo());
        if (in.getColumn() != null) out.setColumn(in.getColumn());
        return out;
    }

    /**
     * Converts ECLGraph from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLGraph
     *            in
     * @return ECLGraph
     */
    private ECLGraph convertECLGraph(org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLGraph in)
    {
        ECLGraph out = new ECLGraph();
        if (in.getName() != null) out.setName(in.getName());
        if (in.getLabel() != null) out.setLabel(in.getLabel());
        if (in.getType() != null) out.setType(in.getType());
        if (in.getRunning() != null) out.setRunning(in.getRunning());
        if (in.getComplete() != null) out.setComplete(in.getComplete());
        if (in.getFailed() != null) out.setFailed(in.getFailed());
        if (in.getRunningId() != null) out.setRunningId(in.getRunningId());
        if (in.getWhenStarted() != null) out.setWhenStarted(in.getWhenStarted());
        if (in.getWhenFinished() != null) out.setWhenFinished(in.getWhenFinished());
        return out;
    }

    /**
     * Converts ECLSchemaItem from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLSchemaItem
     *            in
     * @return ECLSchemaItem
     */
    private ECLSchemaItem convertECLSchemaItem(org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLSchemaItem in)
    {
        ECLSchemaItem out = new ECLSchemaItem();
        if (in.getColumnName() != null) out.setColumnName(in.getColumnName());
        if (in.getColumnType() != null) out.setColumnType(in.getColumnType());
        if (in.getColumnTypeCode() != null) out.setColumnTypeCode(in.getColumnTypeCode());
        if (in.getIsConditional() != null) out.setIsConditional(in.getIsConditional());
        return out;
    }

    /**
     * Converts ECLResult from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLResult
     *            in
     * @return ECLResult
     */
    private ECLResult convertECLResult(org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLResult in)
    {
        ECLResult out = new ECLResult();
        if (in.getName() != null) out.setName(in.getName());
        if (in.getSequence() != null) out.setSequence(in.getSequence());
        if (in.getValue() != null) out.setValue(in.getValue());
        if (in.getLink() != null) out.setLink(in.getLink());
        if (in.getFileName() != null) out.setFileName(in.getFileName());
        if (in.getIsSupplied() != null) out.setIsSupplied(in.getIsSupplied());
        if (in.getShowFileContent() != null) out.setShowFileContent(in.getShowFileContent());
        if (in.getTotal() != null) out.setTotal(in.getTotal());
        if (in.getXmlSchema() != null) out.setXmlSchema(in.getXmlSchema());
        if (in.getECLSchemas() != null)
        {
            ECLSchemaItem[] teclschemaitems = new ECLSchemaItem[in.getECLSchemas().length];
            for (int i = 0; i < in.getECLSchemas().length; i++)
                teclschemaitems[i] = convertECLSchemaItem(in.getECLSchemas()[i]);
            out.setECLSchemas(teclschemaitems);
        }
        return out;
    }

    /**
     * Converts ECLSourceFile from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLSourceFile
     *            in
     * @return ECLSourceFile
     */
    private ECLSourceFile convertECLSourceFile(org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLSourceFile in)
    {
        ECLSourceFile out = new ECLSourceFile();
        if (in.getFileCluster() != null) out.setFileCluster(in.getFileCluster());
        if (in.getName() != null) out.setName(in.getName());
        if (in.getIsSuperFile() != null) out.setIsSuperFile(in.getIsSuperFile());
        if (in.getSubs() != null) out.setSubs(in.getSubs());
        if (in.getCount() != null) out.setCount(in.getCount());
        if (in.getECLSourceFiles() != null)
        {
            ECLSourceFile[] teclsourcefile = new ECLSourceFile[in.getECLSourceFiles().length];
            for (int i = 0; i < in.getECLSourceFiles().length; i++)
                teclsourcefile[i] = convertECLSourceFile(in.getECLSourceFiles()[i]);
            out.setECLSourceFiles(teclsourcefile);
        }
        return out;
    }

    /**
     * Converts ECLTimer from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLTimer
     *            in
     * @return ECLTimer
     */
    private ECLTimer convertECLTimer(org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLTimer in)
    {
        ECLTimer out = new ECLTimer();
        if (in.getName() != null) out.setName(in.getName());
        if (in.getValue() != null) out.setValue(in.getValue());
        if (in.getCount() != null) out.setCount(in.getCount());
        if (in.getGraphName() != null) out.setGraphName(in.getGraphName());
        if (in.getSubGraphId() != null) out.setSubGraphId(in.getSubGraphId());
        return out;
    }

    /**
     * Converts DebugValue from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.DebugValue
     *            in
     * @return DebugValue
     */
    private DebugValue convertDebugValue(org.hpccsystems.ws.client.gen.wsworkunits.v1_56.DebugValue in)
    {
        DebugValue out = new DebugValue();
        if (in.getName() != null) out.setName(in.getName());
        if (in.getValue() != null) out.setValue(in.getValue());
        return out;
    }

    /**
     * Converts ApplicationValue from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ApplicationValue
     *            in
     * @return ApplicationValue
     */
    private ApplicationValue convertApplicationValue(
            org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ApplicationValue in)
    {
        ApplicationValue out = new ApplicationValue();
        if (in.getApplication() != null) out.setApplication(in.getApplication());
        if (in.getName() != null) out.setName(in.getName());
        if (in.getValue() != null) out.setValue(in.getValue());
        return out;
    }

    /**
     * Converts ECLWorkflow from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLWorkflow
     *            in
     * @return ECLWorkflow
     */
    private ECLWorkflow convertECLWorkflow(org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLWorkflow in)
    {
        ECLWorkflow out = new ECLWorkflow();
        if (in.getWFID() != null) out.setWFID(in.getWFID());
        if (in.getEventName() != null) out.setEventName(in.getEventName());
        if (in.getEventText() != null) out.setEventText(in.getEventText());
        if (in.getCount() != null) out.setCount(in.getCount());
        if (in.getCountRemaining() != null) out.setCountRemaining(in.getCountRemaining());
        return out;
    }

    /**
     * Converts ECLTimingData from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLTimingData
     *            in
     * @return ECLTimingData
     */
    private ECLTimingData convertECLTimingData(org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLTimingData in)
    {
        ECLTimingData out = new ECLTimingData();
        if (in.getName() != null) out.setName(in.getName());
        if (in.getGraphNum() != null) out.setGraphNum(in.getGraphNum());
        if (in.getSubGraphNum() != null) out.setSubGraphNum(in.getSubGraphNum());
        if (in.getGID() != null) out.setGID(in.getGID());
        if (in.getMin() != null) out.setMin(in.getMin());
        if (in.getMS() != null) out.setMS(in.getMS());
        return out;
    }

    /**
     * Converts ECLWorkunit from v1_56 to v1_58
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLWorkunit
     *            in
     * @return ECLWorkunit
     */
    private ECLWorkunit convertECLWorkunit(org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLWorkunit in)
    {
        ECLWorkunit out = new ECLWorkunit();

        if (in.getWuid() != null) out.setWuid(in.getWuid());
        if (in.getOwner() != null) out.setOwner(in.getOwner());
        if (in.getCluster() != null) out.setCluster(in.getCluster());
        if (in.getRoxieCluster() != null) out.setRoxieCluster(in.getRoxieCluster());
        if (in.getJobname() != null) out.setJobname(in.getJobname());
        if (in.getQueue() != null) out.setQueue(in.getQueue());
        if (in.getStateID() != null) out.setStateID(in.getStateID());
        if (in.getState() != null) out.setState(in.getState());
        if (in.getStateEx() != null) out.setStateEx(in.getStateEx());
        if (in.getDescription() != null) out.setDescription(in.getDescription());
        if (in.get_protected() != null) out.set_protected(in.get_protected());
        if (in.getActive() != null) out.setActive(in.getActive());
        if (in.getAction() != null) out.setAction(in.getAction());
        if (in.getActionEx() != null) out.setActionEx(in.getActionEx());
        if (in.getDateTimeScheduled() != null) out.setDateTimeScheduled(in.getDateTimeScheduled());
        if (in.getPriorityClass() != null) out.setPriorityClass(in.getPriorityClass());
        if (in.getPriorityLevel() != null) out.setPriorityLevel(in.getPriorityLevel());
        if (in.getScope() != null) out.setScope(in.getScope());
        if (in.getSnapshot() != null) out.setSnapshot(in.getSnapshot());
        if (in.getResultLimit() != null) out.setResultLimit(in.getResultLimit());
        if (in.getArchived() != null) out.setArchived(in.getArchived());
        if (in.getIsPausing() != null) out.setIsPausing(in.getIsPausing());
        if (in.getThorLCR() != null) out.setThorLCR(in.getThorLCR());
        if (in.getEventSchedule() != null) out.setEventSchedule(in.getEventSchedule());
        if (in.getHaveSubGraphTimings() != null) out.setHaveSubGraphTimings(in.getHaveSubGraphTimings());
        if (in.getTotalClusterTime() != null) out.setTotalClusterTime(in.getTotalClusterTime());
        if (in.getAllowedClusters() != null) out.setAllowedClusters(in.getAllowedClusters());
        if (in.getErrorCount() != null) out.setErrorCount(in.getErrorCount());
        if (in.getWarningCount() != null) out.setWarningCount(in.getWarningCount());
        if (in.getInfoCount() != null) out.setInfoCount(in.getInfoCount());
        if (in.getAlertCount() != null) out.setAlertCount(in.getAlertCount());
        if (in.getGraphCount() != null) out.setGraphCount(in.getGraphCount());
        if (in.getSourceFileCount() != null) out.setSourceFileCount(in.getSourceFileCount());
        if (in.getResultCount() != null) out.setResultCount(in.getResultCount());
        if (in.getVariableCount() != null) out.setVariableCount(in.getVariableCount());
        if (in.getTimerCount() != null) out.setTimerCount(in.getTimerCount());
        if (in.getHasDebugValue() != null) out.setHasDebugValue(in.getHasDebugValue());
        if (in.getApplicationValueCount() != null) out.setApplicationValueCount(in.getApplicationValueCount());
        if (in.getXmlParams() != null) out.setXmlParams(in.getXmlParams());
        if (in.getAccessFlag() != null) out.setAccessFlag(in.getAccessFlag());
        if (in.getClusterFlag() != null) out.setClusterFlag(in.getClusterFlag());
        if (in.getHelpersDesc() != null) out.setHelpersDesc(in.getHelpersDesc());
        if (in.getGraphsDesc() != null) out.setGraphsDesc(in.getGraphsDesc());
        if (in.getSourceFilesDesc() != null) out.setSourceFilesDesc(in.getSourceFilesDesc());
        if (in.getResultsDesc() != null) out.setResultsDesc(in.getResultsDesc());
        if (in.getVariablesDesc() != null) out.setVariablesDesc(in.getVariablesDesc());
        if (in.getTimersDesc() != null) out.setTimersDesc(in.getTimersDesc());
        if (in.getDebugValuesDesc() != null) out.setDebugValuesDesc(in.getDebugValuesDesc());
        if (in.getApplicationValuesDesc() != null) out.setApplicationValuesDesc(in.getApplicationValuesDesc());
        if (in.getWorkflowsDesc() != null) out.setWorkflowsDesc(in.getWorkflowsDesc());
        if (in.getHasArchiveQuery() != null) out.setHasArchiveQuery(in.getHasArchiveQuery());
        if (in.getResourceURLs() != null) out.setResourceURLs(in.getResourceURLs());
        if (in.getResultViewCount() != null) out.setResultViewCount(in.getResultViewCount());
        if (in.getResourceURLCount() != null) out.setResourceURLCount(in.getResourceURLCount());
        if (in.getDebugValueCount() != null) out.setDebugValueCount(in.getDebugValueCount());
        if (in.getWorkflowCount() != null) out.setWorkflowCount(in.getWorkflowCount());
        if (in.getQuery() != null) out.setQuery(convertECLQuery(in.getQuery()));

        if (in.getThorLogList() != null)
        {
            ThorLogInfo[] tli = new ThorLogInfo[in.getThorLogList().length];
            for (int i = 0; i < in.getThorLogList().length; i++)
                tli[i] = convertThorLogInfo(in.getThorLogList()[i]);
            out.setThorLogList(tli);
        }
        if (in.getHelpers() != null)
        {
            ECLHelpFile[] teclhf = new ECLHelpFile[in.getHelpers().length];
            for (int i = 0; i < in.getHelpers().length; i++)
                teclhf[i] = convertECLHelpFile(in.getHelpers()[i]);
            out.setHelpers(teclhf);
        }
        if (in.getExceptions() != null)
        {
            ECLException[] te = new ECLException[in.getExceptions().length];
            for (int i = 0; i < in.getExceptions().length; i++)
                te[i] = convertECLException(in.getExceptions()[i]);
            out.setExceptions(te);
        }
        if (in.getGraphs() != null)
        {
            ECLGraph[] tg = new ECLGraph[in.getGraphs().length];
            for (int i = 0; i < in.getGraphs().length; i++)
                tg[i] = convertECLGraph(in.getGraphs()[i]);
            out.setGraphs(tg);
        }

        if (in.getSourceFiles() != null)
        {
            ECLSourceFile[] teclsf = new ECLSourceFile[in.getSourceFiles().length];
            for (int i = 0; i < in.getSourceFiles().length; i++)
                teclsf[i] = convertECLSourceFile(in.getSourceFiles()[i]);
            out.setSourceFiles(teclsf);
        }
        if (in.getResults() != null)
        {
            ECLResult[] tresults = new ECLResult[in.getResults().length];
            for (int i = 0; i < in.getResults().length; i++)
                tresults[i] = convertECLResult(in.getResults()[i]);
            out.setResults(tresults);
        }
        if (in.getVariables() != null)
        {
            ECLResult[] tvariables = new ECLResult[in.getVariables().length];
            for (int i = 0; i < in.getVariables().length; i++)
                tvariables[i] = convertECLResult(in.getVariables()[i]);
            out.setVariables(tvariables);
        }
        if (in.getTimers() != null)
        {
            ECLTimer[] tecltimer = new ECLTimer[in.getTimers().length];
            for (int i = 0; i < in.getTimers().length; i++)
                tecltimer[i] = convertECLTimer(in.getTimers()[i]);
            out.setTimers(tecltimer);
        }
        if (in.getDebugValues() != null)
        {
            DebugValue[] tdebugvalue = new DebugValue[in.getDebugValues().length];
            for (int i = 0; i < in.getDebugValues().length; i++)
                tdebugvalue[i] = convertDebugValue(in.getDebugValues()[i]);
            out.setDebugValues(tdebugvalue);
        }
        if (in.getApplicationValues() != null)
        {
            ApplicationValue[] tapplicationvalues = new ApplicationValue[in.getApplicationValues().length];
            for (int i = 0; i < in.getApplicationValues().length; i++)
                tapplicationvalues[i] = convertApplicationValue(in.getApplicationValues()[i]);
            out.setApplicationValues(tapplicationvalues);
        }
        if (in.getWorkflows() != null)
        {
            ECLWorkflow[] teclworkflows = new ECLWorkflow[in.getWorkflows().length];
            for (int i = 0; i < in.getWorkflows().length; i++)
                teclworkflows[i] = convertECLWorkflow(in.getWorkflows()[i]);
            out.setWorkflows(teclworkflows);
        }
        if (in.getTimingData() != null)
        {
            ECLTimingData[] tecltimingdatas = new ECLTimingData[in.getTimingData().length];
            for (int i = 0; i < in.getTimingData().length; i++)
                tecltimingdatas[i] = convertECLTimingData(in.getTimingData()[i]);
            out.setTimingData(tecltimingdatas);
        }

        return out;
    }

    /**
     * Convert from latest wuquery response to 1.56 wuquery response
     * 
     * @param org.hpccsystems.ws.client.gen.wsworkunits.v1_56.WUQueryResponse
     *            in
     * @return WUQueryResponse
     */
    private WUQueryResponse convertWUQueryResponse(org.hpccsystems.ws.client.gen.wsworkunits.v1_56.WUQueryResponse in)
    {
        WUQueryResponse out = new WUQueryResponse();

        if (in.getType() != null) out.setType(in.getType());
        if (in.getCluster() != null) out.setCluster(in.getCluster());
        if (in.getRoxieCluster() != null) out.setRoxieCluster(in.getRoxieCluster());
        if (in.getOwner() != null) out.setOwner(in.getOwner());
        if (in.getState() != null) out.setState(in.getState());
        if (in.getStartDate() != null) out.setStartDate(in.getStartDate());
        if (in.getEndDate() != null) out.setEndDate(in.getEndDate());
        if (in.getECL() != null) out.setECL(in.getECL());
        if (in.getJobname() != null) out.setJobname(in.getJobname());
        if (in.getLogicalFile() != null) out.setLogicalFile(in.getLogicalFile());
        if (in.getLogicalFileSearchType() != null) out.setLogicalFileSearchType(in.getLogicalFileSearchType());
        if (in.getCurrent() != null) out.setCurrent(in.getCurrent());
        if (in.getNext() != null) out.setNext(in.getNext());
        if (in.getCount() != null) out.setCount(in.getCount());
        if (in.getPageSize() != null) out.setPageSize(in.getPageSize());
        if (in.getPrevPage() != null) out.setPrevPage(in.getPrevPage());
        if (in.getNextPage() != null) out.setNextPage(in.getNextPage());
        if (in.getLastPage() != null) out.setLastPage(in.getLastPage());
        if (in.getNumWUs() != null) out.setNumWUs(in.getNumWUs());
        if (in.getFirst() != null) out.setFirst(in.getFirst());
        if (in.getPageStartFrom() != null) out.setPageStartFrom(in.getPageStartFrom());
        if (in.getPageEndAt() != null) out.setPageEndAt(in.getPageEndAt());
        if (in.getLastNDays() != null) out.setLastNDays(in.getLastNDays());
        if (in.getSortby() != null) out.setSortby(in.getSortby());
        if (in.getDescending() != null) out.setDescending(in.getDescending());
        if (in.getBasicQuery() != null) out.setBasicQuery(in.getBasicQuery());
        if (in.getFilters() != null) out.setFilters(in.getFilters());
        if (in.getCacheHint() != null) out.setCacheHint(in.getCacheHint());
        if (in.getExceptions() != null) out.setExceptions(convertArrayOfEspException(in.getExceptions()));
        if (in.getWorkunits() != null)
        {
            ECLWorkunit[] tmpwu = new ECLWorkunit[in.getWorkunits().length];
            for (int i = 0; i < in.getWorkunits().length; i++)
                tmpwu[i] = convertECLWorkunit(in.getWorkunits()[i]);
            out.setWorkunits(tmpwu);
        }

        return out;
    }

    /**
     * Check the version found at instatiation with compatibility version
     * 
     * @param Version
     *            input
     * @return boolean true if server build version >= input version
     */
    private boolean compatibilityCheck(Version input)
    {
        if (targetVersion.major > input.major)
            return true;
        else if (targetVersion.major < input.major) return false;
        if (targetVersion.minor > input.minor)
            return true;
        else if (targetVersion.minor < input.minor) return false;
        if (targetVersion.point > input.point)
            return true;
        else if (targetVersion.point < input.point)
            return false;
        else
            return true;
    }

    /**
     * As of Platform version 6.0.0. Now maps to new prototype, excluding the pageSize and startPageFrom parameters.
     * Executes a WUQuery, based on parameters provided. If a custom WUQuery is desired, the caller can make a direct
     * call to WUQuery based on the soapproxy for this client. If using applicationValues objects for filtering, a user
     * must pair the Application with either an attribute name, or attribute value.
     *
     * @param wuid
     * @param jobname
     * @param cluster
     * @param type
     * @param sortby
     * @param state
     * @param endDate
     * @param startDate
     * @param pageStartFrom
     * @param pageSize
     * @param count
     * @param owner
     * @param applicationValues
     * @return WUQueryResponse
     * @throws Exception
     */
    public WUQueryResponse workUnitUQuery(String wuid, String jobname, String cluster, String type, String sortby,
            String state, String endDate, String startDate, Long pageStartFrom, Long pageSize, Integer count,
            String owner, ApplicationValue[] applicationValues) throws Exception
    {
        Long _pageSize = pageSize;
        if (_pageSize == null) _pageSize = (long) 10000; // default to 10,000 if null
        Long _pageStartFrom = pageStartFrom;
        if (_pageStartFrom == null) _pageStartFrom = (long) 0;

        WUQueryResponse wuQueryResponse = new WUQueryResponse();
        if (wsWorkunitsServiceSoapProxy == null)
            throw new Exception("wsWorkunitsServiceSoapProxy not available");
        else
        {
            // sanitize ApplicationValue[]
            if (applicationValues != null) for (ApplicationValue v : applicationValues)
            {
                if (v.getApplication() != null && !v.getApplication().isEmpty()
                        && ((v.getName() == null || v.getName().isEmpty())
                                && (v.getValue() == null || v.getValue().isEmpty())))
                    throw new Exception("Application set but no name/value given");
            }

            // Check esp build version
            ArrayOfEspException arrayOfEspException = null;

            if (!compatibilityCheck(new Version("6.0.0")))
            {
                // fall back to old interface
                org.hpccsystems.ws.client.gen.wsworkunits.v1_56.WUQuery internal = new org.hpccsystems.ws.client.gen.wsworkunits.v1_56.WUQuery();
                org.hpccsystems.ws.client.gen.wsworkunits.v1_56.WUQueryResponse fallbackresponse = null;
                // build common elements
                if (cluster != null) internal.setCluster(cluster);
                if (type != null) internal.setType(type);
                if (state != null) internal.setState(state);
                if (startDate != null) internal.setStartDate(startDate);
                if (endDate != null) internal.setEndDate(endDate);
                if (wuid != null) internal.setWuid(wuid);
                if (jobname != null) internal.setJobname(jobname);
                if (count != null) internal.setCount(count);
                if (owner != null) internal.setOwner(owner);
                internal.setPageSize(_pageSize); // warning, if setting pageSize, the output of the sets won't be
                                                 // guaranteed to contain all matches before intersection
                Set<org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLWorkunit> workunit_set = new HashSet<org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLWorkunit>();
                if (applicationValues != null)
                {
                    for (ApplicationValue apfilter : applicationValues)
                    {
                        internal.setApplicationName(apfilter.getApplication());
                        internal.setApplicationKey(apfilter.getName());
                        internal.setApplicationData(apfilter.getValue());

                        fallbackresponse = fallBackWorkunitsServiceSoapProxy.WUQuery(internal);
                        if (fallbackresponse.getExceptions() != null)
                            throwWsWUExceptions(convertArrayOfEspException(fallbackresponse.getExceptions()),
                                    "Error in WU query");
                        Set<org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLWorkunit> internal_wu_set = new HashSet<org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLWorkunit>();
                        if (fallbackresponse.getWorkunits() != null)
                            for (org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLWorkunit wu : fallbackresponse
                                    .getWorkunits())
                            internal_wu_set.add(wu);
                        if (workunit_set.isEmpty())
                            workunit_set.addAll(internal_wu_set);
                        else
                            // intersection of s1 and s2
                            workunit_set.retainAll(internal_wu_set);
                    }
                }
                else
                {
                    fallbackresponse = fallBackWorkunitsServiceSoapProxy.WUQuery(internal);
                    if (fallbackresponse.getExceptions() != null)
                        throwWsWUExceptions(convertArrayOfEspException(fallbackresponse.getExceptions()),
                                "Error in WU query");
                    if (fallbackresponse.getWorkunits() != null)
                        for (org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLWorkunit wu : fallbackresponse
                                .getWorkunits())
                        workunit_set.add(wu);
                }

                // rebuild response as latest
                org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLWorkunit[] workunits = workunit_set
                        .toArray(new org.hpccsystems.ws.client.gen.wsworkunits.v1_56.ECLWorkunit[workunit_set.size()]);
                fallbackresponse.setWorkunits(workunits);
                fallbackresponse.setNumWUs(fallbackresponse.getWorkunits().length);
                wuQueryResponse = convertWUQueryResponse(fallbackresponse);
            }
            else
            {

                WUQuery request = new WUQuery();
                if (cluster != null) request.setCluster(cluster);
                if (type != null) request.setType(type);
                if (sortby != null) request.setSortby(sortby);
                if (state != null) request.setState(state);
                if (startDate != null) request.setStartDate(startDate);
                if (endDate != null) request.setEndDate(endDate);
                if (wuid != null) request.setWuid(wuid);
                if (jobname != null) request.setJobname(jobname);
                if (count != null) request.setCount(count);
                if (owner != null) request.setOwner(owner);
                if (applicationValues != null) request.setApplicationValues(applicationValues);
                request.setPageSize(_pageSize);
                request.setPageStartFrom(_pageStartFrom);

                wuQueryResponse = wsWorkunitsServiceSoapProxy.WUQuery(request);
                arrayOfEspException = wuQueryResponse.getExceptions();
                if (arrayOfEspException != null) throwWsWUExceptions(arrayOfEspException, "Error in WU query");
            }
        }
        return wuQueryResponse;
    }

    public WUQueryResponse workUnitUQuery(WUQuery params) throws Exception
    {
        return this.workUnitUQuery(params.getWuid(), params.getJobname(), params.getCluster(), params.getType(),
                params.getSortby(), params.getState(), params.getEndDate(), params.getStartDate(),
                params.getPageStartFrom(), params.getPageSize(), params.getCount(), params.getOwner(),
                params.getApplicationValues());
    }

    /**
     * @deprecated Executes a WUQuery, based on parameters provided. If a custom WUQuery is desired, the caller can make
     *             a direct call to WUQuery based on the soapproxy for this client.
     *
     * @param wuid
     * @param jobname
     * @param cluster
     * @param type
     * @param sortby
     * @param state
     * @param endDate
     * @param startDate
     * @param pageStartFrom
     * @param pageSize
     * @param count
     * @param owner
     * @param appName
     * @param appKey
     * @param appData
     * @return WUQueryResponse
     * @throws Exception
     */
    @Deprecated
    public WUQueryResponse workUnitUQuery(String wuid, String jobname, String cluster, String type, String sortby,
            String state, String endDate, String startDate, Long pageStartFrom, Long pageSize, Integer count,
            String owner, String appName, String appKey, String appData) throws Exception
    {
        ApplicationValue[] applicationValues = { new ApplicationValue(appKey, appName, appData) };
        return this.workUnitUQuery(wuid, jobname, cluster, type, sortby, state, endDate, startDate, pageStartFrom,
                pageSize, count, owner, applicationValues);
    }

    /**
     * This method queries information about a given WU and answers if the WU has reported errors.
     *
     * @param wuid
     *            - The ID of the target workunit
     * @return - true if errors present
     * @throws Exception
     *             - Caller must handle exceptions
     */
    public boolean doesWUContainErrors(String wuid) throws Exception
    {
        boolean errsfound = true;
        WorkunitInfo workunit = getWUInfo(wuid);

        if (workunit != null) errsfound = workunit.getErrorCount().intValue() <= 0;

        return errsfound;
    }

    /**
     * Requests target HPCC System to create and compile WU based on ecl provided.
     *
     * @param ecl
     * @param targetCluster
     * @param resultLimit
     * @param debugValues
     * @param jobname
     * @return
     * @throws Exception
     */
    public WorkunitInfo createWUFromECL(WorkunitInfo wu) throws Exception
    {
        WorkunitInfo createdWU = null;

        if (wsWorkunitsServiceSoapProxy == null)
            throw new Exception("wsWorkunitsServiceSoapProxy not available");
        else
        {
            WUCreateAndUpdate wucreateparameters = new WUCreateAndUpdate();
            wucreateparameters.setAction(1); // 1= compile
            wucreateparameters.setQueryText(wu.getECL());
            wucreateparameters.setApplicationValues(wu.getApplicationValues());
            wucreateparameters.setDebugValues(wu.getDebugValues());
            wucreateparameters.setJobname(wu.getJobname());
            wucreateparameters.setClusterOrig(wu.getCluster());
            wucreateparameters.setResultLimit(wu.getResultLimit());

            WUUpdateResponse wuUpdateResponse = wsWorkunitsServiceSoapProxy.WUCreateAndUpdate(wucreateparameters);

            ArrayOfEspException exceptions = wuUpdateResponse.getExceptions();
            if (exceptions == null)
            {
                createdWU = new WorkunitInfo(wuUpdateResponse.getWorkunit());
                createdWU.setMaxMonitorMillis(wu.getMaxMonitorMillis());
                createdWU.setCluster(wu.getCluster());
                createdWU.setJobname(wu.getJobname());
                createdWU.setSleepMillis(wu.getSleepMillis());
                createdWU.setOriginalEclWatchUrl(getEclWatchUrl());

            }
            else
            {
                throwWsWUExceptions(exceptions, "Error compiling ECL query");
            }
        }

        return createdWU;
    }

    /*
     * this method is purely for the Platform class
     */
    public WUUpdateResponse createWUFromECL(String archiveOrEcl, int resultLimit, ApplicationValue[] appVals,
            String jobName, boolean compileOnly) throws Exception
    {
        if (wsWorkunitsServiceSoapProxy == null)
            throw new Exception("wsWorkunitsServiceSoapProxy not available");
        else
        {
            WUCreateAndUpdate request = new WUCreateAndUpdate();
            request.setQueryText(archiveOrEcl);
            request.setJobname(jobName);
            if (compileOnly) request.setAction(1);

            request.setApplicationValues(appVals);
            if (resultLimit > 0) request.setResultLimit(resultLimit);

            return wsWorkunitsServiceSoapProxy.WUCreateAndUpdate(request);
        }
    }

    /**
     * @param jobName
     *            the pattern to find in the jobname (wildcard * matches multiple jobnames)
     * @param owner
     *            - hpcc owner of the job
     * @param ecl
     *            - text in the ecl
     * @return an ArrayList<ECLWorkunit> of matching workunits
     * @throws Exception
     */
    public ArrayList<WorkunitInfo> getWorkunits(String jobName, String owner, String ecl) throws Exception
    {
        WUQuery params = new WUQuery();
        if (jobName != null)
        {
            params.setJobname(jobName);
        }

        if (owner != null)
        {
            params.setOwner(owner);
        }

        if (ecl != null)
        {
            params.setECL(ecl);
        }
        return getWorkunits(params);
    }

    public ArrayList<WorkunitInfo> getWorkunits(WUQuery params) throws Exception
    {
        ArrayList<WorkunitInfo> wks = new ArrayList<WorkunitInfo>();

        try
        {
            WUQueryResponse result = this.workUnitUQuery(params);
            ECLWorkunit[] ecls = result.getWorkunits();

            if (ecls == null)
            {
                return wks;
            }
            for (int i = 0; i < ecls.length; i++)
            {
                WorkunitInfo w = new WorkunitInfo(ecls[i]);
                w.setOriginalEclWatchUrl(getEclWatchUrl());
                wks.add(w);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        return wks;
    }

    /**
     * Given an ECL query, this method will create a WU, compile and run it
     *
     * @param ecl
     *            - The ecl query to compile/run
     * @param targetCluster
     *            - The cluster where the ecl is to be compiled/run
     * @return - Object with WU information
     * @throws Exception
     *             - Caller must handle exceptions
     */
    public WorkunitInfo compileWUFromECL(WorkunitInfo wu) throws Exception
    {
        Integer sleeptime = wu.getSleepMillis();

        if (sleeptime == null)
        {
            sleeptime = defaultWaitTime;
        }

        WorkunitInfo createdWU = createWUFromECL(wu);

        if (createdWU != null && createdWU.getErrorCount() == 0 && createdWU.getExceptions() == null)
        {
            createdWU.setCluster(wu.getCluster());
            submitWU(createdWU); // if no exception proceed
            this.monitorWUToCompletion(createdWU);

            // exceptions, etc. aren't always included in the submit response; do another request to get all workunit
            // info
            WUInfoResponse res = this.getWUInfo(createdWU.getWuid(), false, false, false, false, false, true, false,
                    false, false);
            if (createdWU.getExceptions() == null && res.getWorkunit() != null
                    && res.getWorkunit().getExceptions() != null)
            {
                int actualerrors = 0;
                for (int i = 0; i < res.getWorkunit().getExceptions().length; i++)
                {
                    if ("error".equalsIgnoreCase(res.getWorkunit().getExceptions()[i].getSeverity()))
                    {
                        actualerrors++;
                    }
                }
                if (actualerrors > 0)
                {
                    this.throwWUECLExceptions(res.getWorkunit().getExceptions(), "Workunit Compile Failed");
                }
            }
        }
        return createdWU;
    }

    /**
     * Given an ECL query, this method will create a WU, compile and run it
     *
     * @param ecl
     *            - The ecl query to compile/run
     * @param targetCluster
     *            - The cluster where the ecl is to be compiled/run
     * @return - Object with WU information
     * @throws Exception
     *             - Caller must handle exceptions
     */
    public void submitWU(WorkunitInfo wu) throws Exception
    {
        submitWU(wu.getWuid(), wu.getCluster());
    }

    public void submitWU(String wuid, String cluster) throws Exception
    {
        if (wsWorkunitsServiceSoapProxy == null)
            throw new Exception("wsWorkunitsServiceSoapProxy not available");
        else
        {
            WUSubmit wuSubmitParams = new WUSubmit();
            wuSubmitParams.setWuid(wuid);
            wuSubmitParams.setCluster(cluster);

            WUSubmitResponse submitResponse = wsWorkunitsServiceSoapProxy.WUSubmit(wuSubmitParams);

            ArrayOfEspException exceptions = submitResponse.getExceptions();
            if (exceptions != null)
            {
                throwWsWUExceptions(exceptions, "Error compiling ECL query");
            }
        }
    }

    /**
     * Given an ECL query, this method will create a WU, compile and run it
     *
     * @param ecl
     *            - The ecl query to compile/run
     * @param targetCluster
     *            - The cluster where the ecl is to be compiled/run
     * @param resultLimit
     *            - Limit on results
     * @return - Object with WU information
     * @throws Exception
     *             - Caller must handle exceptions
     */
    public WURunResponse createAndRunWUFromECL(WorkunitInfo wu) throws Exception
    {
        WURunResponse wuRunResponse = null;

        if (wsWorkunitsServiceSoapProxy == null)
            throw new Exception("wsWorkunitsServiceSoapProxy not available");
        else
        {
            WorkunitInfo compiledWU = null;
            compiledWU = compileWUFromECL(wu);

            if (compiledWU != null)
            {
                WURun runparameters = new WURun();
                runparameters.setWuid(compiledWU.getWuid());
                runparameters.setVariables(wu.getNamedValues());
                runparameters.setCluster(wu.getCluster());

                wuRunResponse = wsWorkunitsServiceSoapProxy.WURun(runparameters);

                ArrayOfEspException exceptions = wuRunResponse.getExceptions();
                if (exceptions != null)
                {
                    throwWsWUExceptions(exceptions, "Error running WUID: " + compiledWU.getWuid());
                }
                else if (isWorkunitFailed(wuRunResponse.getState()))
                {
                    String error = wuRunResponse.getResults();
                    throw new Exception("Error running WUID: " + compiledWU.getWuid() + ":\n" + error);
                }
            }
        }

        return wuRunResponse;
    }

    /**
     * Given an ECL query, this method will create a WU, compile and run it, and return results in String object - Not
     * recommended for large resultsets.
     *
     * @param ecl
     *            - The ecl query to compile/run
     * @param targetCluster
     *            - The cluster where the ecl is to be compiled/run
     * @param resultLimit
     *            - Limit on results
     * @return - Object with WU information
     * @throws Exception
     *             - Caller must handle exceptions
     */
    public String createAndRunWUFromECLAndGetResults(WorkunitInfo wu) throws Exception
    {
        WURunResponse createAndRunWUFromECL = createAndRunWUFromECL(wu);
        ArrayOfEspException exceptions = createAndRunWUFromECL.getExceptions();
        if (exceptions != null) throwWsWUExceptions(exceptions, "Results contains errors!");
        return createAndRunWUFromECL.getResults();
    }

    /**
     * Given an ECL query, this method will create a WU, compile and run it, and return wuid. Workuinit state, and
     * results can be queried based on WUID
     *
     * @param ecl
     *            - The ecl query to compile/run
     * @param targetCluster
     *            - The cluster where the ecl is to be compiled/run
     * @param resultLimit
     *            - Limit on results
     * @return - Workunit ID
     * @throws Exception
     *             - Caller must handle exceptions
     */
    public String createAndRunWUFromECLAndGetWUID(WorkunitInfo wu) throws Exception
    {
        WURunResponse createAndRunWUFromECL = createAndRunWUFromECL(wu);
        return createAndRunWUFromECL.getWuid();
    }

    /**
     * Fetches results associated with a given Logical name
     *
     * @param wuid
     *            - The target WUID to fetch results from
     * @param sequence
     *            - Zero based index result sequence (Each result set may contain multiple results)
     * @param cluster
     *            - The target cluster
     * @param suppressXMLShema
     *            - Results are accompanied by a schema, do you want to suppress it?
     * @param resultOffset
     *            - Request results starting at record offset
     * @param resultCount
     *            - Total result record count
     * @return - Results in xml string
     * @throws Exception
     */
    public String fetchResultsFromLogicalName(String logicalName, int sequence, String cluster,
            boolean suppressXMLShema, long resultOffset, int resultCount) throws Exception
    {
        WUResultResponse wuResultResponse = fetchRawResults(logicalName, false, sequence, cluster, suppressXMLShema,
                resultOffset, resultCount);
        ArrayOfEspException exceptions = wuResultResponse.getExceptions();
        if (exceptions != null) throwWsWUExceptions(exceptions, "Could not fetch results");

        return wuResultResponse.getResult();
    }

    /**
     * Fetches results associated with a given WUID If the given WUID has been archived, results might not be available
     * using this function Use fetchResultsFromLogicalName instead
     *
     * @param wuid
     *            - The target WUID to fetch results from
     * @param sequence
     *            - Zero based index result sequence (Each result set may contain multiple results)
     * @param cluster
     *            - The target cluster
     * @param suppressXMLShema
     *            - Results are accompanied by a schema, do you want to suppress it?
     * @param resultOffset
     *            - Request results starting at record offset
     * @param resultCount
     *            - Total result record count re
     * @return - Results in xml string
     * @throws Exception
     */
    public String fetchResults(String wuid, int sequence, String cluster, boolean suppressXMLShema, long resultOffset,
            int resultCount) throws Exception
    {
        WUResultResponse wuResultResponse = fetchRawResults(wuid, true, sequence, cluster, suppressXMLShema,
                resultOffset, resultCount);
        ArrayOfEspException exceptions = wuResultResponse.getExceptions();
        if (exceptions != null) throwWsWUExceptions(exceptions, "Could not fetch results");

        return wuResultResponse.getResult();
    }

    public WUResultResponse fetchRawResults(String wuidorlogicalname, boolean useWuid, int sequence, String cluster,
            boolean suppressXMLShema, long resultOffset, int resultCount) throws Exception
    {
        if (wsWorkunitsServiceSoapProxy == null)
            throw new Exception("wsWorkunitsServiceSoapProxy not available");
        else if (wuidorlogicalname == null || wuidorlogicalname.length() == 0)
            throw new Exception("Please provide either WUID or LogicalName");
        else
        {
            WUResult parameters = new WUResult();
            if (useWuid)
                parameters.setWuid(wuidorlogicalname);
            else
                parameters.setLogicalName(wuidorlogicalname);
            parameters.setSequence(sequence);
            parameters.setStart(resultOffset);
            parameters.setCount(resultCount);
            if (cluster != null) parameters.setCluster(cluster);
            parameters.setSuppressXmlSchema(suppressXMLShema);

            return fetchRawResults(parameters);
        }
    }

    /**
     * @param wuid
     *            does this string follow the known WU format
     * @return true if it wuid appears to be a workunit, false otherwise
     *
     *         logic borrowed from platform code: workunit.cpp bool looksLikeAWuid(const char * wuid) { if (!wuid)
     *         return false; if (wuid[0] != 'W') return false; if (!isdigit(wuid[1]) || !isdigit(wuid[2]) ||
     *         !isdigit(wuid[3]) || !isdigit(wuid[4])) return false; if (!isdigit(wuid[5]) || !isdigit(wuid[6]) ||
     *         !isdigit(wuid[7]) || !isdigit(wuid[8])) return false; return (wuid[9]=='-'); }
     */
    public static boolean looksLikeAWuid(String wuid)
    {
        if (wuid == null || wuid.length() == 0) return false;
        if (wuid.charAt(0) != 'W' && wuid.charAt(0) != 'w') return false;
        if (!Character.isDigit(wuid.charAt(1)) || !Character.isDigit(wuid.charAt(2))
                || !Character.isDigit(wuid.charAt(3)) || !Character.isDigit(wuid.charAt(4)))
            return false;
        if (!Character.isDigit(wuid.charAt(5)) || !Character.isDigit(wuid.charAt(6))
                || !Character.isDigit(wuid.charAt(7)) || !Character.isDigit(wuid.charAt(8)))
            return false;
        return (wuid.charAt(9) == '-');
    }

    public WUResultResponse fetchRawResults(WUResult parameters) throws Exception
    {
        if (wsWorkunitsServiceSoapProxy == null)
            throw new Exception("wsWorkunitsServiceSoapProxy not available");
        else
            return wsWorkunitsServiceSoapProxy.WUResult(parameters);
    }

    /**
     * Creates and throws exception with exception message response from WS
     *
     * @param wsWUResponseExceptions
     * @param message
     * @throws Exception
     */
    private void throwWsWUExceptions(ArrayOfEspException wsWUResponseExceptions, String message) throws Exception
    {
        StringBuilder multimessage = new StringBuilder();
        multimessage.append(message);
        multimessage.append("\n");
        for (org.hpccsystems.ws.client.gen.wsworkunits.v1_62.EspException exception : wsWUResponseExceptions
                .getException())
        {
            multimessage.append("\t");
            multimessage.append(exception.getMessage());
        }

        throw new Exception(multimessage.toString());
    }

    /**
     * Creates and throws exception with exception message response from WS
     *
     * @param eclExceptions
     *            - the array of ECLException objects to throw
     * @param message
     *            - the prefix message
     * @throws Exception
     */
    private void throwWUECLExceptions(ECLException[] eclExceptions, String message) throws Exception
    {
        if (eclExceptions == null)
        {
            return;
        }
        StringBuilder multimessage = new StringBuilder();
        multimessage.append(message);
        multimessage.append("\n");
        for (int i = 0; i < eclExceptions.length; i++)
        {
            multimessage.append("\t");
            multimessage.append(eclExceptions[i].getMessage());
        }

        throw new Exception(multimessage.toString());
    }

    private void refreshWU(boolean full, WorkunitInfo wu) throws Exception
    {
        if (full || HPCCWsWorkUnitsClient.isWorkunitComplete(wu))
        {
            fullWURefresh(wu);
        }
        else
        {
            fastWURefresh(wu);
        }
    }

    private void monitorWUToCompletion(WorkunitInfo wu) throws Exception
    {
        int timerTickCount = 0;
        String id = wu.getWuid();
        if (wu.getJobname() != null)
        {
            id = id + "(" + wu.getJobname() + ")";
        }
        Utils.println(System.out, "Monitoring WU " + id + " to completion.", false, verbose);
        while (!HPCCWsWorkUnitsClient.isWorkunitComplete(wu))
        {
            try
            {
                Utils.println(System.out, "Monitoring WU " + id + " to completion ( " + timerTickCount + ") ...", true,
                        verbose);
                Thread.sleep(wu.getSleepMillis());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            timerTickCount++;
            if (timerTickCount * wu.getSleepMillis() > wu.getMaxMonitorMillis())
                throw new Exception("Timed out waiting for WUID " + wu.getWuid() + " to complete.");

            try
            {
                if (timerTickCount == 1)
                    refreshWU(true, wu);
                else if (timerTickCount < 5 && timerTickCount % 1 == 0)
                    refreshWU(false, wu);
                else if (timerTickCount < 30 && timerTickCount % 5 == 0)
                    refreshWU(false, wu);
                else if (timerTickCount < 60 && timerTickCount % 10 == 0)
                    refreshWU(false, wu);
                else if (timerTickCount < 120 && timerTickCount % 30 == 0)
                    refreshWU(true, wu);
                else if (timerTickCount % 60 == 0) refreshWU(true, wu);
            }
            catch (Exception e)
            {
                throw new Exception("Error attempting to refresh WU " + wu.getWuid());
            }
        }
    }

    public ECLException[] syntaxCheckECL(String ecl, String cluster) throws Exception
    {
        if (wsWorkunitsServiceSoapProxy == null)
            throw new Exception("wsWorkunitsServiceSoapProxy not available");
        else
        {
            WUSyntaxCheckECL checkParams = new WUSyntaxCheckECL();
            checkParams.setECL(ecl);
            checkParams.setCluster(cluster);
            WUSyntaxCheckResponse resp = wsWorkunitsServiceSoapProxy.WUSyntaxCheckECL(checkParams);
            return resp.getErrors();
        }

    }

    public WUQuerySetDetailsResponse getQueriesDetail(String querySetName, String clusterName, String filter)
            throws Exception
    {
        if (wsWorkunitsServiceSoapProxy == null) throw new Exception("wsWorkunitsServiceSoapProxy not available");

        WUQuerysetDetails wuQuerysetDetails = new WUQuerysetDetails();
        wuQuerysetDetails.setQuerySetName(querySetName);
        wuQuerysetDetails.setClusterName(clusterName);
        wuQuerysetDetails.setFilter(filter);

        return wsWorkunitsServiceSoapProxy.WUQuerysetDetails(wuQuerysetDetails);
    }

    public void abortWU(String wuid) throws Exception
    {
        if (wsWorkunitsServiceSoapProxy == null) throw new Exception("wsWorkunitsServiceSoapProxy not available");

        WUAbort request = new WUAbort();
        String[] wuids = new String[1];
        wuids[0] = wuid;
        request.setWuids(wuids);

        wsWorkunitsServiceSoapProxy.WUAbort(request);
    }

    public void deleteWU(String wuid) throws Exception
    {
        if (wsWorkunitsServiceSoapProxy == null) throw new Exception("wsWorkunitsServiceSoapProxy not available");

        WUDelete request = new WUDelete();
        String[] wuids = new String[1];
        wuids[0] = wuid;
        request.setWuids(wuids);

        wsWorkunitsServiceSoapProxy.WUDelete(request);
    }

    public void resubmitWU(String wuid, boolean restart, boolean clone) throws Exception
    {
        if (wsWorkunitsServiceSoapProxy == null) throw new Exception("wsWorkunitsServiceSoapProxy not available");

        WUResubmit request = new WUResubmit();
        request.setResetWorkflow(restart);
        request.setCloneWorkunit(clone);
        String[] wuids = new String[1];
        wuids[0] = wuid;
        request.setWuids(wuids);

        wsWorkunitsServiceSoapProxy.WUResubmit(request);
    }

    public QuerySet[] getQuerySets() throws Exception
    {
        if (wsWorkunitsServiceSoapProxy == null) throw new Exception("wsWorkunitsServiceSoapProxy not available");

        QuerySet[] queryset = null;
        WUQuerysets request = new WUQuerysets();
        WUQuerysetsResponse response = wsWorkunitsServiceSoapProxy.WUQuerysets(request);
        if (response != null)
        {
            queryset = response.getQuerysets();
        }

        return queryset;
    }

    @Override
    protected boolean isComplete()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void fastRefresh()
    {
        // TODO Auto-generated method stub
    }

    @Override
    protected void fullRefresh()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean equals(Object aThat)
    {
        if (this == aThat)
        {
            return true;
        }

        if (!(aThat instanceof HPCCWsWorkUnitsClient))
        {
            return false;
        }

        HPCCWsWorkUnitsClient that = (HPCCWsWorkUnitsClient) aThat;
        WsWorkunitsServiceSoapProxy thatSoapProxy;
        try
        {
            thatSoapProxy = that.getSoapProxy();
        }
        catch (Exception e)
        {
            thatSoapProxy = null;
        }

        return EqualsUtil.areEqual(wsWorkunitsServiceSoapProxy.getEndpoint(), thatSoapProxy.getEndpoint())
                && EqualsUtil.areEqual(((Stub) wsWorkunitsServiceSoapProxy.getWsWorkunitsServiceSoap()).getUsername(),
                        ((Stub) thatSoapProxy.getWsWorkunitsServiceSoap()).getUsername())
                && EqualsUtil.areEqual(((Stub) wsWorkunitsServiceSoapProxy.getWsWorkunitsServiceSoap()).getPassword(),
                        ((Stub) thatSoapProxy.getWsWorkunitsServiceSoap()).getPassword());
    }

    @Override
    public int hashCode()
    {
        int result = HashCodeUtil.SEED;
        result = HashCodeUtil.hash(result, wsWorkunitsServiceSoapProxy.getEndpoint());
        result = HashCodeUtil.hash(result,
                ((Stub) wsWorkunitsServiceSoapProxy.getWsWorkunitsServiceSoap()).getUsername());
        result = HashCodeUtil.hash(result,
                ((Stub) wsWorkunitsServiceSoapProxy.getWsWorkunitsServiceSoap()).getPassword());
        return result;
    }

    /**
     * Return a workunit result
     *
     * @param WUResult
     * @return WUResultResponse
     * @throws Exception
     */
    public WUResultResponse getWorkunitResult(WUResult wur) throws Exception
    {
        return getWorkunitResult(wur, false);
    }

    /**
     * Return a workunit result
     *
     * @param WUResult
     * @param unarchive
     * @return WUResultResponse
     * @throws Exception
     */
    public WUResultResponse getWorkunitResult(WUResult wur, boolean unarchive) throws Exception
    {
        // get the object first to make sure it's not archived
        if (unarchive)
        {
            WorkunitInfo wu = this.getWUInfo(wur.getWuid(), unarchive);
        }
        // get the response
        WUResultResponse resp = getSoapProxy().getWsWorkunitsServiceSoap().WUResult(wur);
        return resp;
    }

    public boolean doWorkunitAction(String wuid, ECLWUActions action)
            throws ArrayOfEspException, RemoteException, Exception
    {
        WUAction wa = new WUAction();
        wa.setWUActionType(action);
        wa.setWuids(new String[] { wuid });
        WUActionResponse war = this.getSoapProxy().getWsWorkunitsServiceSoap().WUAction(wa);
        if (war == null || war.getActionResults() == null || war.getActionResults().length == 0
                || war.getActionResults()[0].getResult() == null
                || !war.getActionResults()[0].getResult().equals("Success"))
        {
        	// fallback to 1.56 api
        	org.hpccsystems.ws.client.gen.wsworkunits.v1_56.WUAction fwa = new org.hpccsystems.ws.client.gen.wsworkunits.v1_56.WUAction();
        	fwa.setActionType(action.getValue());
        	fwa.setWuids(new String[] { wuid });
        	org.hpccsystems.ws.client.gen.wsworkunits.v1_56.WUActionResponse fwar = this.fallBackWorkunitsServiceSoapProxy.getWsWorkunitsServiceSoap().WUAction(fwa);
        	if (fwar == null || fwar.getActionResults() == null || fwar.getActionResults().length == 0
        			|| fwar.getActionResults()[0].getResult() == null
        			|| !fwar.getActionResults()[0].getResult().equals("Success"))
        	{
        		throw new Exception("Unable to perform " + action.getValue() + " on " + wuid);
        	}
        }
        return true;
    }

    private String getEclWatchUrl() throws Exception
    {
        String url = this.getSoapProxy().getEndpoint().toLowerCase().replace("wsworkunits", "");
        return url;
    }
}
