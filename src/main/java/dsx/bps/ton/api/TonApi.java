package dsx.bps.ton.api;

public class TonApi {
    /**
     * This class is a base class for all tonlib interface classes.
     */
    public abstract static class Object {
        /**
         * @return string representation of the object.
         */
        public native String toString();

        /**
         * @return identifier uniquely determining type of the object.
         */
        public abstract int getConstructor();
    }

    /**
     * This class is a base class for all tonlib interface function-classes.
     */
    public abstract static class Function extends Object {
        /**
         * @return string representation of the object.
         */
        public native String toString();
    }

    /**
     *
     */
    public static class AccountAddress extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 755613099;
        public String accountAddress;

        /**
         *
         */
        public AccountAddress() {
        }

        public AccountAddress(String accountAddress) {
            this.accountAddress = accountAddress;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class AccountRevisionList extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 120583012;
        public int[] revisions;

        /**
         *
         */
        public AccountRevisionList() {
        }

        public AccountRevisionList(int[] revisions) {
            this.revisions = revisions;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public abstract static class AccountState extends Object {
    }

    public static class RawAccountState extends AccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -531917254;
        public byte[] code;
        public byte[] data;
        public byte[] frozenHash;

        /**
         *
         */
        public RawAccountState() {
        }

        public RawAccountState(byte[] code, byte[] data, byte[] frozenHash) {
            this.code = code;
            this.data = data;
            this.frozenHash = frozenHash;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class TestWalletAccountState extends AccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2053909931;
        public int seqno;

        /**
         *
         */
        public TestWalletAccountState() {
        }

        public TestWalletAccountState(int seqno) {
            this.seqno = seqno;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class WalletAccountState extends AccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -390017192;
        public int seqno;

        /**
         *
         */
        public WalletAccountState() {
        }

        public WalletAccountState(int seqno) {
            this.seqno = seqno;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class WalletV3AccountState extends AccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1619351478;
        public long walletId;
        public int seqno;

        /**
         *
         */
        public WalletV3AccountState() {
        }

        public WalletV3AccountState(long walletId, int seqno) {
            this.walletId = walletId;
            this.seqno = seqno;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class WalletHighloadV1AccountState extends AccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1616372956;
        public long walletId;
        public int seqno;

        /**
         *
         */
        public WalletHighloadV1AccountState() {
        }

        public WalletHighloadV1AccountState(long walletId, int seqno) {
            this.walletId = walletId;
            this.seqno = seqno;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class WalletHighloadV2AccountState extends AccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1803723441;
        public long walletId;

        /**
         *
         */
        public WalletHighloadV2AccountState() {
        }

        public WalletHighloadV2AccountState(long walletId) {
            this.walletId = walletId;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class TestGiverAccountState extends AccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -696813142;
        public int seqno;

        /**
         *
         */
        public TestGiverAccountState() {
        }

        public TestGiverAccountState(int seqno) {
            this.seqno = seqno;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class DnsAccountState extends AccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1727715434;
        public long walletId;

        /**
         *
         */
        public DnsAccountState() {
        }

        public DnsAccountState(long walletId) {
            this.walletId = walletId;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class UninitedAccountState extends AccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1522374408;
        public byte[] frozenHash;

        /**
         *
         */
        public UninitedAccountState() {
        }

        public UninitedAccountState(byte[] frozenHash) {
            this.frozenHash = frozenHash;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public abstract static class Action extends Object {
    }

    public static class ActionNoop extends Action {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1135848603;

        /**
         *
         */
        public ActionNoop() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class ActionMsg extends Action {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 246839120;
        public MsgMessage[] messages;
        public boolean allowSendToUninited;

        /**
         *
         */
        public ActionMsg() {
        }

        public ActionMsg(MsgMessage[] messages, boolean allowSendToUninited) {
            this.messages = messages;
            this.allowSendToUninited = allowSendToUninited;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class ActionDns extends Action {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1193750561;
        public DnsAction[] actions;

        /**
         *
         */
        public ActionDns() {
        }

        public ActionDns(DnsAction[] actions) {
            this.actions = actions;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class AdnlAddress extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 70358284;
        public String adnlAddress;

        /**
         *
         */
        public AdnlAddress() {
        }

        public AdnlAddress(String adnlAddress) {
            this.adnlAddress = adnlAddress;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class Bip39Hints extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1012243456;
        public String[] words;

        /**
         *
         */
        public Bip39Hints() {
        }

        public Bip39Hints(String[] words) {
            this.words = words;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class Config extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1538391496;
        public String config;
        public String blockchainName;
        public boolean useCallbacksForNetwork;
        public boolean ignoreCache;

        /**
         *
         */
        public Config() {
        }

        public Config(String config, String blockchainName, boolean useCallbacksForNetwork, boolean ignoreCache) {
            this.config = config;
            this.blockchainName = blockchainName;
            this.useCallbacksForNetwork = useCallbacksForNetwork;
            this.ignoreCache = ignoreCache;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class Data extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -414733967;
        public byte[] bytes;

        /**
         *
         */
        public Data() {
        }

        public Data(byte[] bytes) {
            this.bytes = bytes;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class Error extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1679978726;
        public int code;
        public String message;

        /**
         *
         */
        public Error() {
        }

        public Error(int code, String message) {
            this.code = code;
            this.message = message;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class ExportedEncryptedKey extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 2024406612;
        public byte[] data;

        /**
         *
         */
        public ExportedEncryptedKey() {
        }

        public ExportedEncryptedKey(byte[] data) {
            this.data = data;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class ExportedKey extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1449248297;
        public String[] wordList;

        /**
         *
         */
        public ExportedKey() {
        }

        public ExportedKey(String[] wordList) {
            this.wordList = wordList;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class ExportedPemKey extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1425473725;
        public String pem;

        /**
         *
         */
        public ExportedPemKey() {
        }

        public ExportedPemKey(String pem) {
            this.pem = pem;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class ExportedUnencryptedKey extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 730045160;
        public byte[] data;

        /**
         *
         */
        public ExportedUnencryptedKey() {
        }

        public ExportedUnencryptedKey(byte[] data) {
            this.data = data;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class Fees extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1676273340;
        public long inFwdFee;
        public long storageFee;
        public long gasFee;
        public long fwdFee;

        /**
         *
         */
        public Fees() {
        }

        public Fees(long inFwdFee, long storageFee, long gasFee, long fwdFee) {
            this.inFwdFee = inFwdFee;
            this.storageFee = storageFee;
            this.gasFee = gasFee;
            this.fwdFee = fwdFee;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class FullAccountState extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -686286006;
        public long balance;
        public InternalTransactionId lastTransactionId;
        public TonBlockIdExt blockId;
        public long syncUtime;
        public AccountState accountState;

        /**
         *
         */
        public FullAccountState() {
        }

        public FullAccountState(long balance, InternalTransactionId lastTransactionId, TonBlockIdExt blockId, long syncUtime, AccountState accountState) {
            this.balance = balance;
            this.lastTransactionId = lastTransactionId;
            this.blockId = blockId;
            this.syncUtime = syncUtime;
            this.accountState = accountState;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public abstract static class InitialAccountState extends Object {
    }

    public static class RawInitialAccountState extends InitialAccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -337945529;
        public byte[] code;
        public byte[] data;

        /**
         *
         */
        public RawInitialAccountState() {
        }

        public RawInitialAccountState(byte[] code, byte[] data) {
            this.code = code;
            this.data = data;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class TestGiverInitialAccountState extends InitialAccountState {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1448412176;

        /**
         *
         */
        public TestGiverInitialAccountState() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class TestWalletInitialAccountState extends InitialAccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 819380068;
        public String publicKey;

        /**
         *
         */
        public TestWalletInitialAccountState() {
        }

        public TestWalletInitialAccountState(String publicKey) {
            this.publicKey = publicKey;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class WalletInitialAccountState extends InitialAccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1122166790;
        public String publicKey;

        /**
         *
         */
        public WalletInitialAccountState() {
        }

        public WalletInitialAccountState(String publicKey) {
            this.publicKey = publicKey;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class WalletV3InitialAccountState extends InitialAccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -118074048;
        public String publicKey;
        public long walletId;

        /**
         *
         */
        public WalletV3InitialAccountState() {
        }

        public WalletV3InitialAccountState(String publicKey, long walletId) {
            this.publicKey = publicKey;
            this.walletId = walletId;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class WalletHighloadV1InitialAccountState extends InitialAccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -327901626;
        public String publicKey;
        public long walletId;

        /**
         *
         */
        public WalletHighloadV1InitialAccountState() {
        }

        public WalletHighloadV1InitialAccountState(String publicKey, long walletId) {
            this.publicKey = publicKey;
            this.walletId = walletId;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class WalletHighloadV2InitialAccountState extends InitialAccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1966373161;
        public String publicKey;
        public long walletId;

        /**
         *
         */
        public WalletHighloadV2InitialAccountState() {
        }

        public WalletHighloadV2InitialAccountState(String publicKey, long walletId) {
            this.publicKey = publicKey;
            this.walletId = walletId;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class DnsInitialAccountState extends InitialAccountState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1842062527;
        public String publicKey;
        public long walletId;

        /**
         *
         */
        public DnsInitialAccountState() {
        }

        public DnsInitialAccountState(String publicKey, long walletId) {
            this.publicKey = publicKey;
            this.walletId = walletId;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public abstract static class InputKey extends Object {
    }

    public static class InputKeyRegular extends InputKey {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -555399522;
        public Key key;
        public byte[] localPassword;

        /**
         *
         */
        public InputKeyRegular() {
        }

        public InputKeyRegular(Key key, byte[] localPassword) {
            this.key = key;
            this.localPassword = localPassword;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class InputKeyFake extends InputKey {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1074054722;

        /**
         *
         */
        public InputKeyFake() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class Key extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1978362923;
        public String publicKey;
        public byte[] secret;

        /**
         *
         */
        public Key() {
        }

        public Key(String publicKey, byte[] secret) {
            this.publicKey = publicKey;
            this.secret = secret;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public abstract static class KeyStoreType extends Object {
    }

    public static class KeyStoreTypeDirectory extends KeyStoreType {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -378990038;
        public String directory;

        /**
         *
         */
        public KeyStoreTypeDirectory() {
        }

        public KeyStoreTypeDirectory(String directory) {
            this.directory = directory;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class KeyStoreTypeInMemory extends KeyStoreType {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2106848825;

        /**
         *
         */
        public KeyStoreTypeInMemory() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * This class is an abstract base class.
     * Describes a stream to which tonlib internal log is written.
     */
    public abstract static class LogStream extends Object {
    }

    /**
     * The log is written to stderr or an OS specific log.
     */
    public static class LogStreamDefault extends LogStream {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1390581436;

        /**
         * The log is written to stderr or an OS specific log.
         */
        public LogStreamDefault() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * The log is written to a file.
     */
    public static class LogStreamFile extends LogStream {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1880085930;
        /**
         * Path to the file to where the internal tonlib log will be written.
         */
        public String path;
        /**
         * Maximum size of the file to where the internal tonlib log is written before the file will be auto-rotated.
         */
        public long maxFileSize;

        /**
         * The log is written to a file.
         */
        public LogStreamFile() {
        }

        /**
         * The log is written to a file.
         *
         * @param path        Path to the file to where the internal tonlib log will be written.
         * @param maxFileSize Maximum size of the file to where the internal tonlib log is written before the file will be auto-rotated.
         */
        public LogStreamFile(String path, long maxFileSize) {
            this.path = path;
            this.maxFileSize = maxFileSize;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * The log is written nowhere.
     */
    public static class LogStreamEmpty extends LogStream {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -499912244;

        /**
         * The log is written nowhere.
         */
        public LogStreamEmpty() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * Contains a list of available tonlib internal log tags.
     */
    public static class LogTags extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1604930601;
        /**
         * List of log tags.
         */
        public String[] tags;

        /**
         * Contains a list of available tonlib internal log tags.
         */
        public LogTags() {
        }

        /**
         * Contains a list of available tonlib internal log tags.
         *
         * @param tags List of log tags.
         */
        public LogTags(String[] tags) {
            this.tags = tags;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * Contains a tonlib internal log verbosity level.
     */
    public static class LogVerbosityLevel extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1734624234;
        /**
         * Log verbosity level.
         */
        public int verbosityLevel;

        /**
         * Contains a tonlib internal log verbosity level.
         */
        public LogVerbosityLevel() {
        }

        /**
         * Contains a tonlib internal log verbosity level.
         *
         * @param verbosityLevel Log verbosity level.
         */
        public LogVerbosityLevel(int verbosityLevel) {
            this.verbosityLevel = verbosityLevel;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class Ok extends Object {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -722616727;

        /**
         *
         */
        public Ok() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class Options extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1924388359;
        public Config config;
        public KeyStoreType keystoreType;

        /**
         *
         */
        public Options() {
        }

        public Options(Config config, KeyStoreType keystoreType) {
            this.config = config;
            this.keystoreType = keystoreType;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public abstract static class SyncState extends Object {
    }

    public static class SyncStateDone extends SyncState {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1408448777;

        /**
         *
         */
        public SyncStateDone() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class SyncStateInProgress extends SyncState {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 107726023;
        public int fromSeqno;
        public int toSeqno;
        public int currentSeqno;

        /**
         *
         */
        public SyncStateInProgress() {
        }

        public SyncStateInProgress(int fromSeqno, int toSeqno, int currentSeqno) {
            this.fromSeqno = fromSeqno;
            this.toSeqno = toSeqno;
            this.currentSeqno = currentSeqno;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class UnpackedAccountAddress extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1892946998;
        public int workchainId;
        public boolean bounceable;
        public boolean testnet;
        public byte[] addr;

        /**
         *
         */
        public UnpackedAccountAddress() {
        }

        public UnpackedAccountAddress(int workchainId, boolean bounceable, boolean testnet, byte[] addr) {
            this.workchainId = workchainId;
            this.bounceable = bounceable;
            this.testnet = testnet;
            this.addr = addr;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public abstract static class Update extends Object {
    }

    public static class UpdateSendLiteServerQuery extends Update {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1555130916;
        public long id;
        public byte[] data;

        /**
         *
         */
        public UpdateSendLiteServerQuery() {
        }

        public UpdateSendLiteServerQuery(long id, byte[] data) {
            this.id = id;
            this.data = data;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class UpdateSyncState extends Update {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1204298718;
        public SyncState syncState;

        /**
         *
         */
        public UpdateSyncState() {
        }

        public UpdateSyncState(SyncState syncState) {
            this.syncState = syncState;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public abstract static class DnsAction extends Object {
    }

    public static class DnsActionDeleteAll extends DnsAction {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1067356318;

        /**
         *
         */
        public DnsActionDeleteAll() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class DnsActionDelete extends DnsAction {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 775206882;
        public String name;
        public int category;

        /**
         *
         */
        public DnsActionDelete() {
        }

        public DnsActionDelete(String name, int category) {
            this.name = name;
            this.category = category;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class DnsActionSet extends DnsAction {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1374965309;
        public DnsEntry entry;

        /**
         *
         */
        public DnsActionSet() {
        }

        public DnsActionSet(DnsEntry entry) {
            this.entry = entry;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class DnsEntry extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1842435400;
        public String name;
        public int category;
        public DnsEntryData entry;

        /**
         *
         */
        public DnsEntry() {
        }

        public DnsEntry(String name, int category, DnsEntryData entry) {
            this.name = name;
            this.category = category;
            this.entry = entry;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public abstract static class DnsEntryData extends Object {
    }

    public static class DnsEntryDataUnknown extends DnsEntryData {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1285893248;
        public byte[] bytes;

        /**
         *
         */
        public DnsEntryDataUnknown() {
        }

        public DnsEntryDataUnknown(byte[] bytes) {
            this.bytes = bytes;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class DnsEntryDataText extends DnsEntryData {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -792485614;
        public String text;

        /**
         *
         */
        public DnsEntryDataText() {
        }

        public DnsEntryDataText(String text) {
            this.text = text;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class DnsEntryDataNextResolver extends DnsEntryData {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 330382792;
        public AccountAddress resolver;

        /**
         *
         */
        public DnsEntryDataNextResolver() {
        }

        public DnsEntryDataNextResolver(AccountAddress resolver) {
            this.resolver = resolver;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class DnsEntryDataSmcAddress extends DnsEntryData {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1759937982;
        public AccountAddress smcAddress;

        /**
         *
         */
        public DnsEntryDataSmcAddress() {
        }

        public DnsEntryDataSmcAddress(AccountAddress smcAddress) {
            this.smcAddress = smcAddress;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class DnsEntryDataAdnlAddress extends DnsEntryData {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1114064368;
        public AdnlAddress adnlAddress;

        /**
         *
         */
        public DnsEntryDataAdnlAddress() {
        }

        public DnsEntryDataAdnlAddress(AdnlAddress adnlAddress) {
            this.adnlAddress = adnlAddress;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class DnsResolved extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 2090272150;
        public DnsEntry[] entries;

        /**
         *
         */
        public DnsResolved() {
        }

        public DnsResolved(DnsEntry[] entries) {
            this.entries = entries;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class TonBlockId extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1185382494;
        public int workchain;
        public long shard;
        public int seqno;

        /**
         *
         */
        public TonBlockId() {
        }

        public TonBlockId(int workchain, long shard, int seqno) {
            this.workchain = workchain;
            this.shard = shard;
            this.seqno = seqno;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class InternalTransactionId extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -989527262;
        public long lt;
        public byte[] hash;

        /**
         *
         */
        public InternalTransactionId() {
        }

        public InternalTransactionId(long lt, byte[] hash) {
            this.lt = lt;
            this.hash = hash;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class LiteServerInfo extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1250165133;
        public long now;
        public int version;
        public long capabilities;

        /**
         *
         */
        public LiteServerInfo() {
        }

        public LiteServerInfo(long now, int version, long capabilities) {
            this.now = now;
            this.version = version;
            this.capabilities = capabilities;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public abstract static class MsgData extends Object {
    }

    public static class MsgDataRaw extends MsgData {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 38878511;
        public byte[] body;

        /**
         *
         */
        public MsgDataRaw() {
        }

        public MsgDataRaw(byte[] body) {
            this.body = body;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class MsgDataText extends MsgData {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -341560688;
        public byte[] text;

        /**
         *
         */
        public MsgDataText() {
        }

        public MsgDataText(byte[] text) {
            this.text = text;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class MsgDataDecryptedText extends MsgData {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1289133895;
        public byte[] text;

        /**
         *
         */
        public MsgDataDecryptedText() {
        }

        public MsgDataDecryptedText(byte[] text) {
            this.text = text;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class MsgDataEncryptedText extends MsgData {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -296612902;
        public byte[] text;

        /**
         *
         */
        public MsgDataEncryptedText() {
        }

        public MsgDataEncryptedText(byte[] text) {
            this.text = text;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class MsgDataArray extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1248461374;
        public MsgData[] elements;

        /**
         *
         */
        public MsgDataArray() {
        }

        public MsgDataArray(MsgData[] elements) {
            this.elements = elements;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class MsgMessage extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1349943761;
        public AccountAddress destination;
        public long amount;
        public MsgData data;

        /**
         *
         */
        public MsgMessage() {
        }

        public MsgMessage(AccountAddress destination, long amount, MsgData data) {
            this.destination = destination;
            this.amount = amount;
            this.data = data;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class OptionsConfigInfo extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 451217371;
        public long defaultWalletId;

        /**
         *
         */
        public OptionsConfigInfo() {
        }

        public OptionsConfigInfo(long defaultWalletId) {
            this.defaultWalletId = defaultWalletId;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class OptionsInfo extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -64676736;
        public OptionsConfigInfo configInfo;

        /**
         *
         */
        public OptionsInfo() {
        }

        public OptionsInfo(OptionsConfigInfo configInfo) {
            this.configInfo = configInfo;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class QueryFees extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1614616510;
        public Fees sourceFees;
        public Fees[] destinationFees;

        /**
         *
         */
        public QueryFees() {
        }

        public QueryFees(Fees sourceFees, Fees[] destinationFees) {
            this.sourceFees = sourceFees;
            this.destinationFees = destinationFees;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class QueryInfo extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1588635915;
        public long id;
        public long validUntil;
        public byte[] bodyHash;

        /**
         *
         */
        public QueryInfo() {
        }

        public QueryInfo(long id, long validUntil, byte[] bodyHash) {
            this.id = id;
            this.validUntil = validUntil;
            this.bodyHash = bodyHash;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class RawFullAccountState extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1465398385;
        public long balance;
        public byte[] code;
        public byte[] data;
        public InternalTransactionId lastTransactionId;
        public TonBlockIdExt blockId;
        public byte[] frozenHash;
        public long syncUtime;

        /**
         *
         */
        public RawFullAccountState() {
        }

        public RawFullAccountState(long balance, byte[] code, byte[] data, InternalTransactionId lastTransactionId, TonBlockIdExt blockId, byte[] frozenHash, long syncUtime) {
            this.balance = balance;
            this.code = code;
            this.data = data;
            this.lastTransactionId = lastTransactionId;
            this.blockId = blockId;
            this.frozenHash = frozenHash;
            this.syncUtime = syncUtime;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class RawMessage extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -32842388;
        public String source;
        public String destination;
        public long value;
        public long fwdFee;
        public long ihrFee;
        public long createdLt;
        public byte[] bodyHash;
        public MsgData msgData;

        /**
         *
         */
        public RawMessage() {
        }

        public RawMessage(String source, String destination, long value, long fwdFee, long ihrFee, long createdLt, byte[] bodyHash, MsgData msgData) {
            this.source = source;
            this.destination = destination;
            this.value = value;
            this.fwdFee = fwdFee;
            this.ihrFee = ihrFee;
            this.createdLt = createdLt;
            this.bodyHash = bodyHash;
            this.msgData = msgData;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class RawTransaction extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1887601793;
        public long utime;
        public byte[] data;
        public InternalTransactionId transactionId;
        public long fee;
        public long storageFee;
        public long otherFee;
        public RawMessage inMsg;
        public RawMessage[] outMsgs;

        /**
         *
         */
        public RawTransaction() {
        }

        public RawTransaction(long utime, byte[] data, InternalTransactionId transactionId, long fee, long storageFee, long otherFee, RawMessage inMsg, RawMessage[] outMsgs) {
            this.utime = utime;
            this.data = data;
            this.transactionId = transactionId;
            this.fee = fee;
            this.storageFee = storageFee;
            this.otherFee = otherFee;
            this.inMsg = inMsg;
            this.outMsgs = outMsgs;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class RawTransactions extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2063931155;
        public RawTransaction[] transactions;
        public InternalTransactionId previousTransactionId;

        /**
         *
         */
        public RawTransactions() {
        }

        public RawTransactions(RawTransaction[] transactions, InternalTransactionId previousTransactionId) {
            this.transactions = transactions;
            this.previousTransactionId = previousTransactionId;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class SmcInfo extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1134270012;
        public long id;

        /**
         *
         */
        public SmcInfo() {
        }

        public SmcInfo(long id) {
            this.id = id;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public abstract static class SmcMethodId extends Object {
    }

    public static class SmcMethodIdNumber extends SmcMethodId {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1541162500;
        public int number;

        /**
         *
         */
        public SmcMethodIdNumber() {
        }

        public SmcMethodIdNumber(int number) {
            this.number = number;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class SmcMethodIdName extends SmcMethodId {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -249036908;
        public String name;

        /**
         *
         */
        public SmcMethodIdName() {
        }

        public SmcMethodIdName(String name) {
            this.name = name;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class SmcRunResult extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1413805043;
        public long gasUsed;
        public TvmStackEntry[] stack;
        public int exitCode;

        /**
         *
         */
        public SmcRunResult() {
        }

        public SmcRunResult(long gasUsed, TvmStackEntry[] stack, int exitCode) {
            this.gasUsed = gasUsed;
            this.stack = stack;
            this.exitCode = exitCode;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class TonBlockIdExt extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 2031156378;
        public int workchain;
        public long shard;
        public int seqno;
        public byte[] rootHash;
        public byte[] fileHash;

        /**
         *
         */
        public TonBlockIdExt() {
        }

        public TonBlockIdExt(int workchain, long shard, int seqno, byte[] rootHash, byte[] fileHash) {
            this.workchain = workchain;
            this.shard = shard;
            this.seqno = seqno;
            this.rootHash = rootHash;
            this.fileHash = fileHash;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class TvmCell extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -413424735;
        public byte[] bytes;

        /**
         *
         */
        public TvmCell() {
        }

        public TvmCell(byte[] bytes) {
            this.bytes = bytes;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class TvmList extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1270320392;
        public TvmStackEntry[] elements;

        /**
         *
         */
        public TvmList() {
        }

        public TvmList(TvmStackEntry[] elements) {
            this.elements = elements;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class TvmNumberDecimal extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1172477619;
        public String number;

        /**
         *
         */
        public TvmNumberDecimal() {
        }

        public TvmNumberDecimal(String number) {
            this.number = number;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class TvmSlice extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 537299687;
        public byte[] bytes;

        /**
         *
         */
        public TvmSlice() {
        }

        public TvmSlice(byte[] bytes) {
            this.bytes = bytes;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public abstract static class TvmStackEntry extends Object {
    }

    public static class TvmStackEntrySlice extends TvmStackEntry {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1395485477;
        public TvmSlice slice;

        /**
         *
         */
        public TvmStackEntrySlice() {
        }

        public TvmStackEntrySlice(TvmSlice slice) {
            this.slice = slice;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class TvmStackEntryCell extends TvmStackEntry {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1303473952;
        public TvmCell cell;

        /**
         *
         */
        public TvmStackEntryCell() {
        }

        public TvmStackEntryCell(TvmCell cell) {
            this.cell = cell;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class TvmStackEntryNumber extends TvmStackEntry {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1358642622;
        public TvmNumberDecimal number;

        /**
         *
         */
        public TvmStackEntryNumber() {
        }

        public TvmStackEntryNumber(TvmNumberDecimal number) {
            this.number = number;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class TvmStackEntryTuple extends TvmStackEntry {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -157391908;
        public TvmTuple tuple;

        /**
         *
         */
        public TvmStackEntryTuple() {
        }

        public TvmStackEntryTuple(TvmTuple tuple) {
            this.tuple = tuple;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class TvmStackEntryList extends TvmStackEntry {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1186714229;
        public TvmList list;

        /**
         *
         */
        public TvmStackEntryList() {
        }

        public TvmStackEntryList(TvmList list) {
            this.list = list;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class TvmStackEntryUnsupported extends TvmStackEntry {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 378880498;

        /**
         *
         */
        public TvmStackEntryUnsupported() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     *
     */
    public static class TvmTuple extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1363953053;
        public TvmStackEntry[] elements;

        /**
         *
         */
        public TvmTuple() {
        }

        public TvmTuple(TvmStackEntry[] elements) {
            this.elements = elements;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * Adds a message to tonlib internal log. This is an offline method. Can be called before authorization. Can be called synchronously.
     *
     * <p> Returns {@link Ok Ok} </p>
     */
    public static class AddLogMessage extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1597427692;
        /**
         * Minimum verbosity level needed for the message to be logged, 0-1023.
         */
        public int verbosityLevel;
        /**
         * Text of a message to log.
         */
        public String text;

        /**
         * Default constructor for a function, which adds a message to tonlib internal log. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public AddLogMessage() {
        }

        /**
         * Creates a function, which adds a message to tonlib internal log. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link Ok Ok} </p>
         *
         * @param verbosityLevel Minimum verbosity level needed for the message to be logged, 0-1023.
         * @param text           Text of a message to log.
         */
        public AddLogMessage(int verbosityLevel, String text) {
            this.verbosityLevel = verbosityLevel;
            this.text = text;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Key Key} </p>
     */
    public static class ChangeLocalPassword extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -401590337;
        public InputKey inputKey;
        public byte[] newLocalPassword;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Key Key} </p>
         */
        public ChangeLocalPassword() {
        }

        public ChangeLocalPassword(InputKey inputKey, byte[] newLocalPassword) {
            this.inputKey = inputKey;
            this.newLocalPassword = newLocalPassword;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Ok Ok} </p>
     */
    public static class Close extends Function {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1187782273;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public Close() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Key Key} </p>
     */
    public static class CreateNewKey extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1861385712;
        public byte[] localPassword;
        public byte[] mnemonicPassword;
        public byte[] randomExtraSeed;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Key Key} </p>
         */
        public CreateNewKey() {
        }

        public CreateNewKey(byte[] localPassword, byte[] mnemonicPassword, byte[] randomExtraSeed) {
            this.localPassword = localPassword;
            this.mnemonicPassword = mnemonicPassword;
            this.randomExtraSeed = randomExtraSeed;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link QueryInfo QueryInfo} </p>
     */
    public static class CreateQuery extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1316835098;
        public InputKey privateKey;
        public AccountAddress address;
        public int timeout;
        public Action action;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link QueryInfo QueryInfo} </p>
         */
        public CreateQuery() {
        }

        public CreateQuery(InputKey privateKey, AccountAddress address, int timeout, Action action) {
            this.privateKey = privateKey;
            this.address = address;
            this.timeout = timeout;
            this.action = action;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Data Data} </p>
     */
    public static class Decrypt extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 357991854;
        public byte[] encryptedData;
        public byte[] secret;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Data Data} </p>
         */
        public Decrypt() {
        }

        public Decrypt(byte[] encryptedData, byte[] secret) {
            this.encryptedData = encryptedData;
            this.secret = secret;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Ok Ok} </p>
     */
    public static class DeleteAllKeys extends Function {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1608776483;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public DeleteAllKeys() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Ok Ok} </p>
     */
    public static class DeleteKey extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1579595571;
        public Key key;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public DeleteKey() {
        }

        public DeleteKey(Key key) {
            this.key = key;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link DnsResolved DnsResolved} </p>
     */
    public static class DnsResolve extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -149238065;
        public AccountAddress accountAddress;
        public String name;
        public int category;
        public int ttl;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link DnsResolved DnsResolved} </p>
         */
        public DnsResolve() {
        }

        public DnsResolve(AccountAddress accountAddress, String name, int category, int ttl) {
            this.accountAddress = accountAddress;
            this.name = name;
            this.category = category;
            this.ttl = ttl;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Data Data} </p>
     */
    public static class Encrypt extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1821422820;
        public byte[] decryptedData;
        public byte[] secret;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Data Data} </p>
         */
        public Encrypt() {
        }

        public Encrypt(byte[] decryptedData, byte[] secret) {
            this.decryptedData = decryptedData;
            this.secret = secret;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link ExportedEncryptedKey ExportedEncryptedKey} </p>
     */
    public static class ExportEncryptedKey extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 218237311;
        public InputKey inputKey;
        public byte[] keyPassword;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link ExportedEncryptedKey ExportedEncryptedKey} </p>
         */
        public ExportEncryptedKey() {
        }

        public ExportEncryptedKey(InputKey inputKey, byte[] keyPassword) {
            this.inputKey = inputKey;
            this.keyPassword = keyPassword;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link ExportedKey ExportedKey} </p>
     */
    public static class ExportKey extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1622353549;
        public InputKey inputKey;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link ExportedKey ExportedKey} </p>
         */
        public ExportKey() {
        }

        public ExportKey(InputKey inputKey) {
            this.inputKey = inputKey;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link ExportedPemKey ExportedPemKey} </p>
     */
    public static class ExportPemKey extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -643259462;
        public InputKey inputKey;
        public byte[] keyPassword;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link ExportedPemKey ExportedPemKey} </p>
         */
        public ExportPemKey() {
        }

        public ExportPemKey(InputKey inputKey, byte[] keyPassword) {
            this.inputKey = inputKey;
            this.keyPassword = keyPassword;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link ExportedUnencryptedKey ExportedUnencryptedKey} </p>
     */
    public static class ExportUnencryptedKey extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -634665152;
        public InputKey inputKey;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link ExportedUnencryptedKey ExportedUnencryptedKey} </p>
         */
        public ExportUnencryptedKey() {
        }

        public ExportUnencryptedKey(InputKey inputKey) {
            this.inputKey = inputKey;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link AccountAddress AccountAddress} </p>
     */
    public static class GetAccountAddress extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1159101819;
        public InitialAccountState initialAccountState;
        public int revision;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link AccountAddress AccountAddress} </p>
         */
        public GetAccountAddress() {
        }

        public GetAccountAddress(InitialAccountState initialAccountState, int revision) {
            this.initialAccountState = initialAccountState;
            this.revision = revision;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link FullAccountState FullAccountState} </p>
     */
    public static class GetAccountState extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2116357050;
        public AccountAddress accountAddress;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link FullAccountState FullAccountState} </p>
         */
        public GetAccountState() {
        }

        public GetAccountState(AccountAddress accountAddress) {
            this.accountAddress = accountAddress;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Bip39Hints Bip39Hints} </p>
     */
    public static class GetBip39Hints extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1889640982;
        public String prefix;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Bip39Hints Bip39Hints} </p>
         */
        public GetBip39Hints() {
        }

        public GetBip39Hints(String prefix) {
            this.prefix = prefix;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * Returns information about currently used log stream for internal logging of tonlib. This is an offline method. Can be called before authorization. Can be called synchronously.
     *
     * <p> Returns {@link LogStream LogStream} </p>
     */
    public static class GetLogStream extends Function {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1167608667;

        /**
         * Default constructor for a function, which returns information about currently used log stream for internal logging of tonlib. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link LogStream LogStream} </p>
         */
        public GetLogStream() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * Returns current verbosity level for a specified tonlib internal log tag. This is an offline method. Can be called before authorization. Can be called synchronously.
     *
     * <p> Returns {@link LogVerbosityLevel LogVerbosityLevel} </p>
     */
    public static class GetLogTagVerbosityLevel extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 951004547;
        /**
         * Logging tag to change verbosity level.
         */
        public String tag;

        /**
         * Default constructor for a function, which returns current verbosity level for a specified tonlib internal log tag. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link LogVerbosityLevel LogVerbosityLevel} </p>
         */
        public GetLogTagVerbosityLevel() {
        }

        /**
         * Creates a function, which returns current verbosity level for a specified tonlib internal log tag. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link LogVerbosityLevel LogVerbosityLevel} </p>
         *
         * @param tag Logging tag to change verbosity level.
         */
        public GetLogTagVerbosityLevel(String tag) {
            this.tag = tag;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * Returns list of available tonlib internal log tags, for example, [&quot;actor&quot;, &quot;binlog&quot;, &quot;connections&quot;, &quot;notifications&quot;, &quot;proxy&quot;]. This is an offline method. Can be called before authorization. Can be called synchronously.
     *
     * <p> Returns {@link LogTags LogTags} </p>
     */
    public static class GetLogTags extends Function {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -254449190;

        /**
         * Default constructor for a function, which returns list of available tonlib internal log tags, for example, [&quot;actor&quot;, &quot;binlog&quot;, &quot;connections&quot;, &quot;notifications&quot;, &quot;proxy&quot;]. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link LogTags LogTags} </p>
         */
        public GetLogTags() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * Returns current verbosity level of the internal logging of tonlib. This is an offline method. Can be called before authorization. Can be called synchronously.
     *
     * <p> Returns {@link LogVerbosityLevel LogVerbosityLevel} </p>
     */
    public static class GetLogVerbosityLevel extends Function {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 594057956;

        /**
         * Default constructor for a function, which returns current verbosity level of the internal logging of tonlib. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link LogVerbosityLevel LogVerbosityLevel} </p>
         */
        public GetLogVerbosityLevel() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link AccountRevisionList AccountRevisionList} </p>
     */
    public static class GuessAccountRevision extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1463344293;
        public InitialAccountState initialAccountState;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link AccountRevisionList AccountRevisionList} </p>
         */
        public GuessAccountRevision() {
        }

        public GuessAccountRevision(InitialAccountState initialAccountState) {
            this.initialAccountState = initialAccountState;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Key Key} </p>
     */
    public static class ImportEncryptedKey extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 656724958;
        public byte[] localPassword;
        public byte[] keyPassword;
        public ExportedEncryptedKey exportedEncryptedKey;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Key Key} </p>
         */
        public ImportEncryptedKey() {
        }

        public ImportEncryptedKey(byte[] localPassword, byte[] keyPassword, ExportedEncryptedKey exportedEncryptedKey) {
            this.localPassword = localPassword;
            this.keyPassword = keyPassword;
            this.exportedEncryptedKey = exportedEncryptedKey;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Key Key} </p>
     */
    public static class ImportKey extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1607900903;
        public byte[] localPassword;
        public byte[] mnemonicPassword;
        public ExportedKey exportedKey;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Key Key} </p>
         */
        public ImportKey() {
        }

        public ImportKey(byte[] localPassword, byte[] mnemonicPassword, ExportedKey exportedKey) {
            this.localPassword = localPassword;
            this.mnemonicPassword = mnemonicPassword;
            this.exportedKey = exportedKey;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Key Key} </p>
     */
    public static class ImportPemKey extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 76385617;
        public byte[] localPassword;
        public byte[] keyPassword;
        public ExportedPemKey exportedKey;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Key Key} </p>
         */
        public ImportPemKey() {
        }

        public ImportPemKey(byte[] localPassword, byte[] keyPassword, ExportedPemKey exportedKey) {
            this.localPassword = localPassword;
            this.keyPassword = keyPassword;
            this.exportedKey = exportedKey;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Key Key} </p>
     */
    public static class ImportUnencryptedKey extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1184671467;
        public byte[] localPassword;
        public ExportedUnencryptedKey exportedUnencryptedKey;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Key Key} </p>
         */
        public ImportUnencryptedKey() {
        }

        public ImportUnencryptedKey(byte[] localPassword, ExportedUnencryptedKey exportedUnencryptedKey) {
            this.localPassword = localPassword;
            this.exportedUnencryptedKey = exportedUnencryptedKey;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link OptionsInfo OptionsInfo} </p>
     */
    public static class Init extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1000594762;
        public Options options;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link OptionsInfo OptionsInfo} </p>
         */
        public Init() {
        }

        public Init(Options options) {
            this.options = options;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Data Data} </p>
     */
    public static class Kdf extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1667861635;
        public byte[] password;
        public byte[] salt;
        public int iterations;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Data Data} </p>
         */
        public Kdf() {
        }

        public Kdf(byte[] password, byte[] salt, int iterations) {
            this.password = password;
            this.salt = salt;
            this.iterations = iterations;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link LiteServerInfo LiteServerInfo} </p>
     */
    public static class LiteServerGetInfo extends Function {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1435327470;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link LiteServerInfo LiteServerInfo} </p>
         */
        public LiteServerGetInfo() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link MsgDataArray MsgDataArray} </p>
     */
    public static class MsgDecrypt extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1131086633;
        public InputKey inputKey;
        public MsgDataArray data;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link MsgDataArray MsgDataArray} </p>
         */
        public MsgDecrypt() {
        }

        public MsgDecrypt(InputKey inputKey, MsgDataArray data) {
            this.inputKey = inputKey;
            this.data = data;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Ok Ok} </p>
     */
    public static class OnLiteServerQueryError extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -677427533;
        public long id;
        public Error error;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public OnLiteServerQueryError() {
        }

        public OnLiteServerQueryError(long id, Error error) {
            this.id = id;
            this.error = error;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Ok Ok} </p>
     */
    public static class OnLiteServerQueryResult extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 2056444510;
        public long id;
        public byte[] bytes;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public OnLiteServerQueryResult() {
        }

        public OnLiteServerQueryResult(long id, byte[] bytes) {
            this.id = id;
            this.bytes = bytes;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link OptionsConfigInfo OptionsConfigInfo} </p>
     */
    public static class OptionsSetConfig extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1870064579;
        public Config config;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link OptionsConfigInfo OptionsConfigInfo} </p>
         */
        public OptionsSetConfig() {
        }

        public OptionsSetConfig(Config config) {
            this.config = config;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link OptionsConfigInfo OptionsConfigInfo} </p>
     */
    public static class OptionsValidateConfig extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -346965447;
        public Config config;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link OptionsConfigInfo OptionsConfigInfo} </p>
         */
        public OptionsValidateConfig() {
        }

        public OptionsValidateConfig(Config config) {
            this.config = config;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link AccountAddress AccountAddress} </p>
     */
    public static class PackAccountAddress extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1388561940;
        public UnpackedAccountAddress accountAddress;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link AccountAddress AccountAddress} </p>
         */
        public PackAccountAddress() {
        }

        public PackAccountAddress(UnpackedAccountAddress accountAddress) {
            this.accountAddress = accountAddress;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link QueryFees QueryFees} </p>
     */
    public static class QueryEstimateFees extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -957002175;
        public long id;
        public boolean ignoreChksig;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link QueryFees QueryFees} </p>
         */
        public QueryEstimateFees() {
        }

        public QueryEstimateFees(long id, boolean ignoreChksig) {
            this.id = id;
            this.ignoreChksig = ignoreChksig;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Ok Ok} </p>
     */
    public static class QueryForget extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1211985313;
        public long id;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public QueryForget() {
        }

        public QueryForget(long id) {
            this.id = id;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link QueryInfo QueryInfo} </p>
     */
    public static class QueryGetInfo extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -799333669;
        public long id;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link QueryInfo QueryInfo} </p>
         */
        public QueryGetInfo() {
        }

        public QueryGetInfo(long id) {
            this.id = id;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Ok Ok} </p>
     */
    public static class QuerySend extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 925242739;
        public long id;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public QuerySend() {
        }

        public QuerySend(long id) {
            this.id = id;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Ok Ok} </p>
     */
    public static class RawCreateAndSendMessage extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -772224603;
        public AccountAddress destination;
        public byte[] initialAccountState;
        public byte[] data;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public RawCreateAndSendMessage() {
        }

        public RawCreateAndSendMessage(AccountAddress destination, byte[] initialAccountState, byte[] data) {
            this.destination = destination;
            this.initialAccountState = initialAccountState;
            this.data = data;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link QueryInfo QueryInfo} </p>
     */
    public static class RawCreateQuery extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1928557909;
        public AccountAddress destination;
        public byte[] initCode;
        public byte[] initData;
        public byte[] body;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link QueryInfo QueryInfo} </p>
         */
        public RawCreateQuery() {
        }

        public RawCreateQuery(AccountAddress destination, byte[] initCode, byte[] initData, byte[] body) {
            this.destination = destination;
            this.initCode = initCode;
            this.initData = initData;
            this.body = body;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link RawFullAccountState RawFullAccountState} </p>
     */
    public static class RawGetAccountState extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1327847118;
        public AccountAddress accountAddress;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link RawFullAccountState RawFullAccountState} </p>
         */
        public RawGetAccountState() {
        }

        public RawGetAccountState(AccountAddress accountAddress) {
            this.accountAddress = accountAddress;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link RawTransactions RawTransactions} </p>
     */
    public static class RawGetTransactions extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1029612317;
        public InputKey privateKey;
        public AccountAddress accountAddress;
        public InternalTransactionId fromTransactionId;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link RawTransactions RawTransactions} </p>
         */
        public RawGetTransactions() {
        }

        public RawGetTransactions(InputKey privateKey, AccountAddress accountAddress, InternalTransactionId fromTransactionId) {
            this.privateKey = privateKey;
            this.accountAddress = accountAddress;
            this.fromTransactionId = fromTransactionId;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Ok Ok} </p>
     */
    public static class RawSendMessage extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1789427488;
        public byte[] body;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public RawSendMessage() {
        }

        public RawSendMessage(byte[] body) {
            this.body = body;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Ok Ok} </p>
     */
    public static class RunTests extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2039925427;
        public String dir;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public RunTests() {
        }

        public RunTests(String dir) {
            this.dir = dir;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * Sets new log stream for internal logging of tonlib. This is an offline method. Can be called before authorization. Can be called synchronously.
     *
     * <p> Returns {@link Ok Ok} </p>
     */
    public static class SetLogStream extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1364199535;
        /**
         * New log stream.
         */
        public LogStream logStream;

        /**
         * Default constructor for a function, which sets new log stream for internal logging of tonlib. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public SetLogStream() {
        }

        /**
         * Creates a function, which sets new log stream for internal logging of tonlib. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link Ok Ok} </p>
         *
         * @param logStream New log stream.
         */
        public SetLogStream(LogStream logStream) {
            this.logStream = logStream;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * Sets the verbosity level for a specified tonlib internal log tag. This is an offline method. Can be called before authorization. Can be called synchronously.
     *
     * <p> Returns {@link Ok Ok} </p>
     */
    public static class SetLogTagVerbosityLevel extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2095589738;
        /**
         * Logging tag to change verbosity level.
         */
        public String tag;
        /**
         * New verbosity level; 1-1024.
         */
        public int newVerbosityLevel;

        /**
         * Default constructor for a function, which sets the verbosity level for a specified tonlib internal log tag. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public SetLogTagVerbosityLevel() {
        }

        /**
         * Creates a function, which sets the verbosity level for a specified tonlib internal log tag. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link Ok Ok} </p>
         *
         * @param tag               Logging tag to change verbosity level.
         * @param newVerbosityLevel New verbosity level; 1-1024.
         */
        public SetLogTagVerbosityLevel(String tag, int newVerbosityLevel) {
            this.tag = tag;
            this.newVerbosityLevel = newVerbosityLevel;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * Sets the verbosity level of the internal logging of tonlib. This is an offline method. Can be called before authorization. Can be called synchronously.
     *
     * <p> Returns {@link Ok Ok} </p>
     */
    public static class SetLogVerbosityLevel extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -303429678;
        /**
         * New value of the verbosity level for logging. Value 0 corresponds to fatal errors, value 1 corresponds to errors, value 2 corresponds to warnings and debug warnings, value 3 corresponds to informational, value 4 corresponds to debug, value 5 corresponds to verbose debug, value greater than 5 and up to 1023 can be used to enable even more logging.
         */
        public int newVerbosityLevel;

        /**
         * Default constructor for a function, which sets the verbosity level of the internal logging of tonlib. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public SetLogVerbosityLevel() {
        }

        /**
         * Creates a function, which sets the verbosity level of the internal logging of tonlib. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link Ok Ok} </p>
         *
         * @param newVerbosityLevel New value of the verbosity level for logging. Value 0 corresponds to fatal errors, value 1 corresponds to errors, value 2 corresponds to warnings and debug warnings, value 3 corresponds to informational, value 4 corresponds to debug, value 5 corresponds to verbose debug, value greater than 5 and up to 1023 can be used to enable even more logging.
         */
        public SetLogVerbosityLevel(int newVerbosityLevel) {
            this.newVerbosityLevel = newVerbosityLevel;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link TvmCell TvmCell} </p>
     */
    public static class SmcGetCode extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2115626088;
        public long id;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link TvmCell TvmCell} </p>
         */
        public SmcGetCode() {
        }

        public SmcGetCode(long id) {
            this.id = id;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link TvmCell TvmCell} </p>
     */
    public static class SmcGetData extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -427601079;
        public long id;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link TvmCell TvmCell} </p>
         */
        public SmcGetData() {
        }

        public SmcGetData(long id) {
            this.id = id;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link TvmCell TvmCell} </p>
     */
    public static class SmcGetState extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -214390293;
        public long id;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link TvmCell TvmCell} </p>
         */
        public SmcGetState() {
        }

        public SmcGetState(long id) {
            this.id = id;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link SmcInfo SmcInfo} </p>
     */
    public static class SmcLoad extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -903491521;
        public AccountAddress accountAddress;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link SmcInfo SmcInfo} </p>
         */
        public SmcLoad() {
        }

        public SmcLoad(AccountAddress accountAddress) {
            this.accountAddress = accountAddress;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link SmcRunResult SmcRunResult} </p>
     */
    public static class SmcRunGetMethod extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -255261270;
        public long id;
        public SmcMethodId method;
        public TvmStackEntry[] stack;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link SmcRunResult SmcRunResult} </p>
         */
        public SmcRunGetMethod() {
        }

        public SmcRunGetMethod(long id, SmcMethodId method, TvmStackEntry[] stack) {
            this.id = id;
            this.method = method;
            this.stack = stack;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link TonBlockIdExt TonBlockIdExt} </p>
     */
    public static class Sync extends Function {

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1875977070;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link TonBlockIdExt TonBlockIdExt} </p>
         */
        public Sync() {
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link UnpackedAccountAddress UnpackedAccountAddress} </p>
     */
    public static class UnpackAccountAddress extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -682459063;
        public String accountAddress;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link UnpackedAccountAddress UnpackedAccountAddress} </p>
         */
        public UnpackAccountAddress() {
        }

        public UnpackAccountAddress(String accountAddress) {
            this.accountAddress = accountAddress;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    /**
     * <p> Returns {@link Object Object} </p>
     */
    public static class WithBlock extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -789093723;
        public TonBlockIdExt id;
        public Function function;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Object Object} </p>
         */
        public WithBlock() {
        }

        public WithBlock(TonBlockIdExt id, Function function) {
            this.id = id;
            this.function = function;
        }

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

}
