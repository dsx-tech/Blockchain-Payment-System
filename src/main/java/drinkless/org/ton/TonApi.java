package drinkless.org.ton;

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
        public String accountAddress;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 755613099;

        public AccountAddress(String accountAddress) {
            this.accountAddress = accountAddress;
        }

        /**
         *
         */
        public AccountAddress() {
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
        public int[] revisions;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 120583012;

        public AccountRevisionList(int[] revisions) {
            this.revisions = revisions;
        }

        /**
         *
         */
        public AccountRevisionList() {
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
        public byte[] code;
        public byte[] data;
        public byte[] frozenHash;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -531917254;

        public RawAccountState(byte[] code, byte[] data, byte[] frozenHash) {
            this.code = code;
            this.data = data;
            this.frozenHash = frozenHash;
        }

        /**
         *
         */
        public RawAccountState() {
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
        public int seqno;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2053909931;

        public TestWalletAccountState(int seqno) {
            this.seqno = seqno;
        }

        /**
         *
         */
        public TestWalletAccountState() {
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
        public int seqno;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -390017192;

        public WalletAccountState(int seqno) {
            this.seqno = seqno;
        }

        /**
         *
         */
        public WalletAccountState() {
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
        public long walletId;
        public int seqno;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1619351478;

        public WalletV3AccountState(long walletId, int seqno) {
            this.walletId = walletId;
            this.seqno = seqno;
        }

        /**
         *
         */
        public WalletV3AccountState() {
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
        public long walletId;
        public int seqno;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1616372956;

        public WalletHighloadV1AccountState(long walletId, int seqno) {
            this.walletId = walletId;
            this.seqno = seqno;
        }

        /**
         *
         */
        public WalletHighloadV1AccountState() {
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
        public long walletId;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1803723441;

        public WalletHighloadV2AccountState(long walletId) {
            this.walletId = walletId;
        }

        /**
         *
         */
        public WalletHighloadV2AccountState() {
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
        public int seqno;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -696813142;

        public TestGiverAccountState(int seqno) {
            this.seqno = seqno;
        }

        /**
         *
         */
        public TestGiverAccountState() {
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
        public long walletId;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1727715434;

        public DnsAccountState(long walletId) {
            this.walletId = walletId;
        }

        /**
         *
         */
        public DnsAccountState() {
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
        public byte[] frozenHash;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1522374408;

        public UninitedAccountState(byte[] frozenHash) {
            this.frozenHash = frozenHash;
        }

        /**
         *
         */
        public UninitedAccountState() {
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
         *
         */
        public ActionNoop() {
        }

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1135848603;

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class ActionMsg extends Action {
        public MsgMessage[] messages;
        public boolean allowSendToUninited;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 246839120;

        public ActionMsg(MsgMessage[] messages, boolean allowSendToUninited) {
            this.messages = messages;
            this.allowSendToUninited = allowSendToUninited;
        }

        /**
         *
         */
        public ActionMsg() {
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
        public DnsAction[] actions;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1193750561;

        public ActionDns(DnsAction[] actions) {
            this.actions = actions;
        }

        /**
         *
         */
        public ActionDns() {
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
        public String adnlAddress;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 70358284;

        public AdnlAddress(String adnlAddress) {
            this.adnlAddress = adnlAddress;
        }

        /**
         *
         */
        public AdnlAddress() {
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
        public String[] words;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1012243456;

        public Bip39Hints(String[] words) {
            this.words = words;
        }

        /**
         *
         */
        public Bip39Hints() {
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
        public String config;
        public String blockchainName;
        public boolean useCallbacksForNetwork;
        public boolean ignoreCache;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1538391496;

        public Config(String config, String blockchainName, boolean useCallbacksForNetwork, boolean ignoreCache) {
            this.config = config;
            this.blockchainName = blockchainName;
            this.useCallbacksForNetwork = useCallbacksForNetwork;
            this.ignoreCache = ignoreCache;
        }

        /**
         *
         */
        public Config() {
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
        public byte[] bytes;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -414733967;

        public Data(byte[] bytes) {
            this.bytes = bytes;
        }

        /**
         *
         */
        public Data() {
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
        public int code;
        public String message;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1679978726;

        public Error(int code, String message) {
            this.code = code;
            this.message = message;
        }

        /**
         *
         */
        public Error() {
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
        public byte[] data;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 2024406612;

        public ExportedEncryptedKey(byte[] data) {
            this.data = data;
        }

        /**
         *
         */
        public ExportedEncryptedKey() {
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
        public String[] wordList;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1449248297;

        public ExportedKey(String[] wordList) {
            this.wordList = wordList;
        }

        /**
         *
         */
        public ExportedKey() {
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
        public String pem;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1425473725;

        public ExportedPemKey(String pem) {
            this.pem = pem;
        }

        /**
         *
         */
        public ExportedPemKey() {
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
        public byte[] data;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 730045160;

        public ExportedUnencryptedKey(byte[] data) {
            this.data = data;
        }

        /**
         *
         */
        public ExportedUnencryptedKey() {
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
        public long inFwdFee;
        public long storageFee;
        public long gasFee;
        public long fwdFee;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1676273340;

        public Fees(long inFwdFee, long storageFee, long gasFee, long fwdFee) {
            this.inFwdFee = inFwdFee;
            this.storageFee = storageFee;
            this.gasFee = gasFee;
            this.fwdFee = fwdFee;
        }

        /**
         *
         */
        public Fees() {
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
        public long balance;
        public InternalTransactionId lastTransactionId;
        public TonBlockIdExt blockId;
        public long syncUtime;
        public AccountState accountState;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -686286006;

        public FullAccountState(long balance, InternalTransactionId lastTransactionId, TonBlockIdExt blockId, long syncUtime, AccountState accountState) {
            this.balance = balance;
            this.lastTransactionId = lastTransactionId;
            this.blockId = blockId;
            this.syncUtime = syncUtime;
            this.accountState = accountState;
        }

        /**
         *
         */
        public FullAccountState() {
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
        public byte[] code;
        public byte[] data;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -337945529;

        public RawInitialAccountState(byte[] code, byte[] data) {
            this.code = code;
            this.data = data;
        }

        /**
         *
         */
        public RawInitialAccountState() {
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
         *
         */
        public TestGiverInitialAccountState() {
        }

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1448412176;

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class TestWalletInitialAccountState extends InitialAccountState {
        public String publicKey;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 819380068;

        public TestWalletInitialAccountState(String publicKey) {
            this.publicKey = publicKey;
        }

        /**
         *
         */
        public TestWalletInitialAccountState() {
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
        public String publicKey;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1122166790;

        public WalletInitialAccountState(String publicKey) {
            this.publicKey = publicKey;
        }

        /**
         *
         */
        public WalletInitialAccountState() {
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
        public String publicKey;
        public long walletId;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -118074048;

        public WalletV3InitialAccountState(String publicKey, long walletId) {
            this.publicKey = publicKey;
            this.walletId = walletId;
        }

        /**
         *
         */
        public WalletV3InitialAccountState() {
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
        public String publicKey;
        public long walletId;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -327901626;

        public WalletHighloadV1InitialAccountState(String publicKey, long walletId) {
            this.publicKey = publicKey;
            this.walletId = walletId;
        }

        /**
         *
         */
        public WalletHighloadV1InitialAccountState() {
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
        public String publicKey;
        public long walletId;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1966373161;

        public WalletHighloadV2InitialAccountState(String publicKey, long walletId) {
            this.publicKey = publicKey;
            this.walletId = walletId;
        }

        /**
         *
         */
        public WalletHighloadV2InitialAccountState() {
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
        public String publicKey;
        public long walletId;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1842062527;

        public DnsInitialAccountState(String publicKey, long walletId) {
            this.publicKey = publicKey;
            this.walletId = walletId;
        }

        /**
         *
         */
        public DnsInitialAccountState() {
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
        public Key key;
        public byte[] localPassword;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -555399522;

        public InputKeyRegular(Key key, byte[] localPassword) {
            this.key = key;
            this.localPassword = localPassword;
        }

        /**
         *
         */
        public InputKeyRegular() {
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
         *
         */
        public InputKeyFake() {
        }

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1074054722;

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
        public String publicKey;
        public byte[] secret;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1978362923;

        public Key(String publicKey, byte[] secret) {
            this.publicKey = publicKey;
            this.secret = secret;
        }

        /**
         *
         */
        public Key() {
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
        public String directory;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -378990038;

        public KeyStoreTypeDirectory(String directory) {
            this.directory = directory;
        }

        /**
         *
         */
        public KeyStoreTypeDirectory() {
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
         *
         */
        public KeyStoreTypeInMemory() {
        }

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2106848825;

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
         * The log is written to stderr or an OS specific log.
         */
        public LogStreamDefault() {
        }

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1390581436;

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
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1880085930;

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
         * The log is written nowhere.
         */
        public LogStreamEmpty() {
        }

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -499912244;

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
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1604930601;

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
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1734624234;

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
         *
         */
        public Ok() {
        }

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -722616727;

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
        public Config config;
        public KeyStoreType keystoreType;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1924388359;

        public Options(Config config, KeyStoreType keystoreType) {
            this.config = config;
            this.keystoreType = keystoreType;
        }

        /**
         *
         */
        public Options() {
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
         *
         */
        public SyncStateDone() {
        }

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1408448777;

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class SyncStateInProgress extends SyncState {
        public int fromSeqno;
        public int toSeqno;
        public int currentSeqno;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 107726023;

        public SyncStateInProgress(int fromSeqno, int toSeqno, int currentSeqno) {
            this.fromSeqno = fromSeqno;
            this.toSeqno = toSeqno;
            this.currentSeqno = currentSeqno;
        }

        /**
         *
         */
        public SyncStateInProgress() {
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
        public int workchainId;
        public boolean bounceable;
        public boolean testnet;
        public byte[] addr;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1892946998;

        public UnpackedAccountAddress(int workchainId, boolean bounceable, boolean testnet, byte[] addr) {
            this.workchainId = workchainId;
            this.bounceable = bounceable;
            this.testnet = testnet;
            this.addr = addr;
        }

        /**
         *
         */
        public UnpackedAccountAddress() {
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
        public long id;
        public byte[] data;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1555130916;

        public UpdateSendLiteServerQuery(long id, byte[] data) {
            this.id = id;
            this.data = data;
        }

        /**
         *
         */
        public UpdateSendLiteServerQuery() {
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
        public SyncState syncState;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1204298718;

        public UpdateSyncState(SyncState syncState) {
            this.syncState = syncState;
        }

        /**
         *
         */
        public UpdateSyncState() {
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
         *
         */
        public DnsActionDeleteAll() {
        }

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1067356318;

        /**
         * @return this.CONSTRUCTOR
         */
        @Override
        public int getConstructor() {
            return CONSTRUCTOR;
        }
    }

    public static class DnsActionDelete extends DnsAction {
        public String name;
        public int category;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 775206882;

        public DnsActionDelete(String name, int category) {
            this.name = name;
            this.category = category;
        }

        /**
         *
         */
        public DnsActionDelete() {
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
        public DnsEntry entry;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1374965309;

        public DnsActionSet(DnsEntry entry) {
            this.entry = entry;
        }

        /**
         *
         */
        public DnsActionSet() {
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
        public String name;
        public int category;
        public DnsEntryData entry;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1842435400;

        public DnsEntry(String name, int category, DnsEntryData entry) {
            this.name = name;
            this.category = category;
            this.entry = entry;
        }

        /**
         *
         */
        public DnsEntry() {
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
        public byte[] bytes;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1285893248;

        public DnsEntryDataUnknown(byte[] bytes) {
            this.bytes = bytes;
        }

        /**
         *
         */
        public DnsEntryDataUnknown() {
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
        public String text;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -792485614;

        public DnsEntryDataText(String text) {
            this.text = text;
        }

        /**
         *
         */
        public DnsEntryDataText() {
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
        public AccountAddress resolver;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 330382792;

        public DnsEntryDataNextResolver(AccountAddress resolver) {
            this.resolver = resolver;
        }

        /**
         *
         */
        public DnsEntryDataNextResolver() {
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
        public AccountAddress smcAddress;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1759937982;

        public DnsEntryDataSmcAddress(AccountAddress smcAddress) {
            this.smcAddress = smcAddress;
        }

        /**
         *
         */
        public DnsEntryDataSmcAddress() {
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
        public AdnlAddress adnlAddress;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1114064368;

        public DnsEntryDataAdnlAddress(AdnlAddress adnlAddress) {
            this.adnlAddress = adnlAddress;
        }

        /**
         *
         */
        public DnsEntryDataAdnlAddress() {
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
        public DnsEntry[] entries;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 2090272150;

        public DnsResolved(DnsEntry[] entries) {
            this.entries = entries;
        }

        /**
         *
         */
        public DnsResolved() {
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
        public int workchain;
        public long shard;
        public int seqno;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1185382494;

        public TonBlockId(int workchain, long shard, int seqno) {
            this.workchain = workchain;
            this.shard = shard;
            this.seqno = seqno;
        }

        /**
         *
         */
        public TonBlockId() {
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
        public long lt;
        public byte[] hash;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -989527262;

        public InternalTransactionId(long lt, byte[] hash) {
            this.lt = lt;
            this.hash = hash;
        }

        /**
         *
         */
        public InternalTransactionId() {
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
        public long now;
        public int version;
        public long capabilities;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1250165133;

        public LiteServerInfo(long now, int version, long capabilities) {
            this.now = now;
            this.version = version;
            this.capabilities = capabilities;
        }

        /**
         *
         */
        public LiteServerInfo() {
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
        public byte[] body;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 38878511;

        public MsgDataRaw(byte[] body) {
            this.body = body;
        }

        /**
         *
         */
        public MsgDataRaw() {
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
        public byte[] text;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -341560688;

        public MsgDataText(byte[] text) {
            this.text = text;
        }

        /**
         *
         */
        public MsgDataText() {
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
        public byte[] text;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1289133895;

        public MsgDataDecryptedText(byte[] text) {
            this.text = text;
        }

        /**
         *
         */
        public MsgDataDecryptedText() {
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
        public byte[] text;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -296612902;

        public MsgDataEncryptedText(byte[] text) {
            this.text = text;
        }

        /**
         *
         */
        public MsgDataEncryptedText() {
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
    public static class MsgDataDecrypted extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 195649769;
        public byte[] proof;
        public MsgData data;

        /**
         *
         */
        public MsgDataDecrypted() {
        }

        public MsgDataDecrypted(byte[] proof, MsgData data) {
            this.proof = proof;
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
    public static class MsgDataDecryptedArray extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -480491767;
        public MsgDataDecrypted[] elements;

        /**
         *
         */
        public MsgDataDecryptedArray() {
        }

        public MsgDataDecryptedArray(MsgDataDecrypted[] elements) {
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
    public static class MsgDataEncrypted extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 564215121;
        public AccountAddress source;
        public MsgData data;

        /**
         *
         */
        public MsgDataEncrypted() {
        }

        public MsgDataEncrypted(AccountAddress source, MsgData data) {
            this.source = source;
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
    public static class MsgDataEncryptedArray extends Object {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 608655794;
        public MsgDataEncrypted[] elements;

        /**
         *
         */
        public MsgDataEncryptedArray() {
        }

        public MsgDataEncryptedArray(MsgDataEncrypted[] elements) {
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
        public AccountAddress destination;
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2110533580;
        public long amount;
        public MsgData data;
        public String publicKey;

        /**
         *
         */
        public MsgMessage() {
        }

        public MsgMessage(AccountAddress destination, String publicKey, long amount, MsgData data) {
            this.destination = destination;
            this.publicKey = publicKey;
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
        public long defaultWalletId;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 451217371;

        public OptionsConfigInfo(long defaultWalletId) {
            this.defaultWalletId = defaultWalletId;
        }

        /**
         *
         */
        public OptionsConfigInfo() {
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
        public OptionsConfigInfo configInfo;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -64676736;

        public OptionsInfo(OptionsConfigInfo configInfo) {
            this.configInfo = configInfo;
        }

        /**
         *
         */
        public OptionsInfo() {
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
        public Fees sourceFees;
        public Fees[] destinationFees;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1614616510;

        public QueryFees(Fees sourceFees, Fees[] destinationFees) {
            this.sourceFees = sourceFees;
            this.destinationFees = destinationFees;
        }

        /**
         *
         */
        public QueryFees() {
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
        public long id;
        public long validUntil;
        public byte[] bodyHash;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1588635915;

        public QueryInfo(long id, long validUntil, byte[] bodyHash) {
            this.id = id;
            this.validUntil = validUntil;
            this.bodyHash = bodyHash;
        }

        /**
         *
         */
        public QueryInfo() {
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
        public long balance;
        public byte[] code;
        public byte[] data;
        public InternalTransactionId lastTransactionId;
        public TonBlockIdExt blockId;
        public byte[] frozenHash;
        public long syncUtime;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1465398385;

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
         *
         */
        public RawFullAccountState() {
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
        public static final int CONSTRUCTOR = 1368093263;
        public AccountAddress source;
        public long value;
        public long fwdFee;
        public long ihrFee;
        public long createdLt;
        public byte[] bodyHash;
        public MsgData msgData;
        public AccountAddress destination;

        /**
         *
         */
        public RawMessage() {
        }

        public RawMessage(AccountAddress source, AccountAddress destination, long value, long fwdFee, long ihrFee, long createdLt, byte[] bodyHash, MsgData msgData) {
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
        public long utime;
        public byte[] data;
        public InternalTransactionId transactionId;
        public long fee;
        public long storageFee;
        public long otherFee;
        public RawMessage inMsg;
        public RawMessage[] outMsgs;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1887601793;

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
         *
         */
        public RawTransaction() {
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
        public RawTransaction[] transactions;
        public InternalTransactionId previousTransactionId;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2063931155;

        public RawTransactions(RawTransaction[] transactions, InternalTransactionId previousTransactionId) {
            this.transactions = transactions;
            this.previousTransactionId = previousTransactionId;
        }

        /**
         *
         */
        public RawTransactions() {
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
        public long id;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1134270012;

        public SmcInfo(long id) {
            this.id = id;
        }

        /**
         *
         */
        public SmcInfo() {
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
        public int number;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1541162500;

        public SmcMethodIdNumber(int number) {
            this.number = number;
        }

        /**
         *
         */
        public SmcMethodIdNumber() {
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
        public String name;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -249036908;

        public SmcMethodIdName(String name) {
            this.name = name;
        }

        /**
         *
         */
        public SmcMethodIdName() {
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
        public long gasUsed;
        public TvmStackEntry[] stack;
        public int exitCode;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1413805043;

        public SmcRunResult(long gasUsed, TvmStackEntry[] stack, int exitCode) {
            this.gasUsed = gasUsed;
            this.stack = stack;
            this.exitCode = exitCode;
        }

        /**
         *
         */
        public SmcRunResult() {
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
        public int workchain;
        public long shard;
        public int seqno;
        public byte[] rootHash;
        public byte[] fileHash;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 2031156378;

        public TonBlockIdExt(int workchain, long shard, int seqno, byte[] rootHash, byte[] fileHash) {
            this.workchain = workchain;
            this.shard = shard;
            this.seqno = seqno;
            this.rootHash = rootHash;
            this.fileHash = fileHash;
        }

        /**
         *
         */
        public TonBlockIdExt() {
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
        public byte[] bytes;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -413424735;

        public TvmCell(byte[] bytes) {
            this.bytes = bytes;
        }

        /**
         *
         */
        public TvmCell() {
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
        public TvmStackEntry[] elements;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1270320392;

        public TvmList(TvmStackEntry[] elements) {
            this.elements = elements;
        }

        /**
         *
         */
        public TvmList() {
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
        public String number;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1172477619;

        public TvmNumberDecimal(String number) {
            this.number = number;
        }

        /**
         *
         */
        public TvmNumberDecimal() {
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
        public byte[] bytes;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 537299687;

        public TvmSlice(byte[] bytes) {
            this.bytes = bytes;
        }

        /**
         *
         */
        public TvmSlice() {
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
        public TvmSlice slice;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1395485477;

        public TvmStackEntrySlice(TvmSlice slice) {
            this.slice = slice;
        }

        /**
         *
         */
        public TvmStackEntrySlice() {
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
        public TvmCell cell;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1303473952;

        public TvmStackEntryCell(TvmCell cell) {
            this.cell = cell;
        }

        /**
         *
         */
        public TvmStackEntryCell() {
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
        public TvmNumberDecimal number;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1358642622;

        public TvmStackEntryNumber(TvmNumberDecimal number) {
            this.number = number;
        }

        /**
         *
         */
        public TvmStackEntryNumber() {
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
        public TvmTuple tuple;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -157391908;

        public TvmStackEntryTuple(TvmTuple tuple) {
            this.tuple = tuple;
        }

        /**
         *
         */
        public TvmStackEntryTuple() {
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
        public TvmList list;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1186714229;

        public TvmStackEntryList(TvmList list) {
            this.list = list;
        }

        /**
         *
         */
        public TvmStackEntryList() {
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
         *
         */
        public TvmStackEntryUnsupported() {
        }

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 378880498;

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
        public TvmStackEntry[] elements;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1363953053;

        public TvmTuple(TvmStackEntry[] elements) {
            this.elements = elements;
        }

        /**
         *
         */
        public TvmTuple() {
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
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1597427692;

        /**
         * Creates a function, which adds a message to tonlib internal log. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link Ok Ok} </p>
         *
         * @param verbosityLevel Minimum verbosity level needed for the message to be logged, 0-1023.
         * @param text Text of a message to log.
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
        public InputKey inputKey;
        public byte[] newLocalPassword;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -401590337;

        public ChangeLocalPassword(InputKey inputKey, byte[] newLocalPassword) {
            this.inputKey = inputKey;
            this.newLocalPassword = newLocalPassword;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Key Key} </p>
         */
        public ChangeLocalPassword() {
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
     *
     *
     * <p> Returns {@link Key Key} </p>
     */
    public static class CreateNewKey extends Function {
        public byte[] localPassword;
        public byte[] mnemonicPassword;
        public byte[] randomExtraSeed;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1861385712;

        public CreateNewKey(byte[] localPassword, byte[] mnemonicPassword, byte[] randomExtraSeed) {
            this.localPassword = localPassword;
            this.mnemonicPassword = mnemonicPassword;
            this.randomExtraSeed = randomExtraSeed;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Key Key} </p>
         */
        public CreateNewKey() {
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
        public InputKey privateKey;
        public AccountAddress address;
        public int timeout;
        public Action action;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1316835098;

        public CreateQuery(InputKey privateKey, AccountAddress address, int timeout, Action action) {
            this.privateKey = privateKey;
            this.address = address;
            this.timeout = timeout;
            this.action = action;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link QueryInfo QueryInfo} </p>
         */
        public CreateQuery() {
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
        public byte[] encryptedData;
        public byte[] secret;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 357991854;

        public Decrypt(byte[] encryptedData, byte[] secret) {
            this.encryptedData = encryptedData;
            this.secret = secret;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Data Data} </p>
         */
        public Decrypt() {
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
        public Key key;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1579595571;

        public DeleteKey(Key key) {
            this.key = key;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public DeleteKey() {
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
     *
     * <p> Returns {@link DnsResolved DnsResolved} </p>
     */
    public static class DnsResolve extends Function {
        public AccountAddress accountAddress;
        public String name;
        public int category;
        public int ttl;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -149238065;

        public DnsResolve(AccountAddress accountAddress, String name, int category, int ttl) {
            this.accountAddress = accountAddress;
            this.name = name;
            this.category = category;
            this.ttl = ttl;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link DnsResolved DnsResolved} </p>
         */
        public DnsResolve() {
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
        public byte[] decryptedData;
        public byte[] secret;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1821422820;

        public Encrypt(byte[] decryptedData, byte[] secret) {
            this.decryptedData = decryptedData;
            this.secret = secret;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Data Data} </p>
         */
        public Encrypt() {
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
        public InputKey inputKey;
        public byte[] keyPassword;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 218237311;

        public ExportEncryptedKey(InputKey inputKey, byte[] keyPassword) {
            this.inputKey = inputKey;
            this.keyPassword = keyPassword;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link ExportedEncryptedKey ExportedEncryptedKey} </p>
         */
        public ExportEncryptedKey() {
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
     *
     * <p> Returns {@link ExportedKey ExportedKey} </p>
     */
    public static class ExportKey extends Function {
        public InputKey inputKey;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1622353549;

        public ExportKey(InputKey inputKey) {
            this.inputKey = inputKey;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link ExportedKey ExportedKey} </p>
         */
        public ExportKey() {
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
        public InputKey inputKey;
        public byte[] keyPassword;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -643259462;

        public ExportPemKey(InputKey inputKey, byte[] keyPassword) {
            this.inputKey = inputKey;
            this.keyPassword = keyPassword;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link ExportedPemKey ExportedPemKey} </p>
         */
        public ExportPemKey() {
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
        public InputKey inputKey;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -634665152;

        public ExportUnencryptedKey(InputKey inputKey) {
            this.inputKey = inputKey;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link ExportedUnencryptedKey ExportedUnencryptedKey} </p>
         */
        public ExportUnencryptedKey() {
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
        public InitialAccountState initialAccountState;
        public int revision;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1159101819;

        public GetAccountAddress(InitialAccountState initialAccountState, int revision) {
            this.initialAccountState = initialAccountState;
            this.revision = revision;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link AccountAddress AccountAddress} </p>
         */
        public GetAccountAddress() {
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
        public AccountAddress accountAddress;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2116357050;

        public GetAccountState(AccountAddress accountAddress) {
            this.accountAddress = accountAddress;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link FullAccountState FullAccountState} </p>
         */
        public GetAccountState() {
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
        public String prefix;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1889640982;

        public GetBip39Hints(String prefix) {
            this.prefix = prefix;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Bip39Hints Bip39Hints} </p>
         */
        public GetBip39Hints() {
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
         * Default constructor for a function, which returns information about currently used log stream for internal logging of tonlib. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link LogStream LogStream} </p>
         */
        public GetLogStream() {
        }

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1167608667;

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
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 951004547;

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
         * Default constructor for a function, which returns list of available tonlib internal log tags, for example, [&quot;actor&quot;, &quot;binlog&quot;, &quot;connections&quot;, &quot;notifications&quot;, &quot;proxy&quot;]. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link LogTags LogTags} </p>
         */
        public GetLogTags() {
        }

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -254449190;

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
         * Default constructor for a function, which returns current verbosity level of the internal logging of tonlib. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link LogVerbosityLevel LogVerbosityLevel} </p>
         */
        public GetLogVerbosityLevel() {
        }

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 594057956;

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
     *
     * <p> Returns {@link AccountRevisionList AccountRevisionList} </p>
     */
    public static class GuessAccountRevision extends Function {
        public InitialAccountState initialAccountState;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1463344293;

        public GuessAccountRevision(InitialAccountState initialAccountState) {
            this.initialAccountState = initialAccountState;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link AccountRevisionList AccountRevisionList} </p>
         */
        public GuessAccountRevision() {
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
        public byte[] localPassword;
        public byte[] keyPassword;
        public ExportedEncryptedKey exportedEncryptedKey;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 656724958;

        public ImportEncryptedKey(byte[] localPassword, byte[] keyPassword, ExportedEncryptedKey exportedEncryptedKey) {
            this.localPassword = localPassword;
            this.keyPassword = keyPassword;
            this.exportedEncryptedKey = exportedEncryptedKey;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Key Key} </p>
         */
        public ImportEncryptedKey() {
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
     *
     * <p> Returns {@link Key Key} </p>
     */
    public static class ImportKey extends Function {
        public byte[] localPassword;
        public byte[] mnemonicPassword;
        public ExportedKey exportedKey;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1607900903;

        public ImportKey(byte[] localPassword, byte[] mnemonicPassword, ExportedKey exportedKey) {
            this.localPassword = localPassword;
            this.mnemonicPassword = mnemonicPassword;
            this.exportedKey = exportedKey;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Key Key} </p>
         */
        public ImportKey() {
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
     *
     * <p> Returns {@link Key Key} </p>
     */
    public static class ImportPemKey extends Function {
        public byte[] localPassword;
        public byte[] keyPassword;
        public ExportedPemKey exportedKey;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 76385617;

        public ImportPemKey(byte[] localPassword, byte[] keyPassword, ExportedPemKey exportedKey) {
            this.localPassword = localPassword;
            this.keyPassword = keyPassword;
            this.exportedKey = exportedKey;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Key Key} </p>
         */
        public ImportPemKey() {
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
     *
     * <p> Returns {@link Key Key} </p>
     */
    public static class ImportUnencryptedKey extends Function {
        public byte[] localPassword;
        public ExportedUnencryptedKey exportedUnencryptedKey;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1184671467;

        public ImportUnencryptedKey(byte[] localPassword, ExportedUnencryptedKey exportedUnencryptedKey) {
            this.localPassword = localPassword;
            this.exportedUnencryptedKey = exportedUnencryptedKey;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Key Key} </p>
         */
        public ImportUnencryptedKey() {
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
        public Options options;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1000594762;

        public Init(Options options) {
            this.options = options;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link OptionsInfo OptionsInfo} </p>
         */
        public Init() {
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
     *
     * <p> Returns {@link Data Data} </p>
     */
    public static class Kdf extends Function {
        public byte[] password;
        public byte[] salt;
        public int iterations;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1667861635;

        public Kdf(byte[] password, byte[] salt, int iterations) {
            this.password = password;
            this.salt = salt;
            this.iterations = iterations;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Data Data} </p>
         */
        public Kdf() {
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
     *
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
     * <p> Returns {@link MsgDataDecryptedArray MsgDataDecryptedArray} </p>
     */
    public static class MsgDecrypt extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 223596297;
        public InputKey inputKey;
        public MsgDataEncryptedArray data;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link MsgDataDecryptedArray MsgDataDecryptedArray} </p>
         */
        public MsgDecrypt() {
        }

        public MsgDecrypt(InputKey inputKey, MsgDataEncryptedArray data) {
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
     *
     *
     * <p> Returns {@link MsgData MsgData} </p>
     */
    public static class MsgDecryptWithProof extends Function {
        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2111649663;
        public byte[] proof;
        public MsgDataEncrypted data;

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link MsgData MsgData} </p>
         */
        public MsgDecryptWithProof() {
        }

        public MsgDecryptWithProof(byte[] proof, MsgDataEncrypted data) {
            this.proof = proof;
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
        public long id;
        public Error error;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -677427533;

        public OnLiteServerQueryError(long id, Error error) {
            this.id = id;
            this.error = error;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public OnLiteServerQueryError() {
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
        public long id;
        public byte[] bytes;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 2056444510;

        public OnLiteServerQueryResult(long id, byte[] bytes) {
            this.id = id;
            this.bytes = bytes;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public OnLiteServerQueryResult() {
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
        public Config config;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1870064579;

        public OptionsSetConfig(Config config) {
            this.config = config;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link OptionsConfigInfo OptionsConfigInfo} </p>
         */
        public OptionsSetConfig() {
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
        public Config config;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -346965447;

        public OptionsValidateConfig(Config config) {
            this.config = config;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link OptionsConfigInfo OptionsConfigInfo} </p>
         */
        public OptionsValidateConfig() {
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
        public UnpackedAccountAddress accountAddress;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1388561940;

        public PackAccountAddress(UnpackedAccountAddress accountAddress) {
            this.accountAddress = accountAddress;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link AccountAddress AccountAddress} </p>
         */
        public PackAccountAddress() {
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
        public long id;
        public boolean ignoreChksig;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -957002175;

        public QueryEstimateFees(long id, boolean ignoreChksig) {
            this.id = id;
            this.ignoreChksig = ignoreChksig;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link QueryFees QueryFees} </p>
         */
        public QueryEstimateFees() {
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
        public long id;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1211985313;

        public QueryForget(long id) {
            this.id = id;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public QueryForget() {
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
        public long id;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -799333669;

        public QueryGetInfo(long id) {
            this.id = id;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link QueryInfo QueryInfo} </p>
         */
        public QueryGetInfo() {
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
        public long id;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 925242739;

        public QuerySend(long id) {
            this.id = id;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public QuerySend() {
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
        public AccountAddress destination;
        public byte[] initialAccountState;
        public byte[] data;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -772224603;

        public RawCreateAndSendMessage(AccountAddress destination, byte[] initialAccountState, byte[] data) {
            this.destination = destination;
            this.initialAccountState = initialAccountState;
            this.data = data;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public RawCreateAndSendMessage() {
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
     *
     * <p> Returns {@link QueryInfo QueryInfo} </p>
     */
    public static class RawCreateQuery extends Function {
        public AccountAddress destination;
        public byte[] initCode;
        public byte[] initData;
        public byte[] body;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1928557909;

        public RawCreateQuery(AccountAddress destination, byte[] initCode, byte[] initData, byte[] body) {
            this.destination = destination;
            this.initCode = initCode;
            this.initData = initData;
            this.body = body;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link QueryInfo QueryInfo} </p>
         */
        public RawCreateQuery() {
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
     *
     * <p> Returns {@link RawFullAccountState RawFullAccountState} </p>
     */
    public static class RawGetAccountState extends Function {
        public AccountAddress accountAddress;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1327847118;

        public RawGetAccountState(AccountAddress accountAddress) {
            this.accountAddress = accountAddress;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link RawFullAccountState RawFullAccountState} </p>
         */
        public RawGetAccountState() {
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
        public InputKey privateKey;
        public AccountAddress accountAddress;
        public InternalTransactionId fromTransactionId;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = 1029612317;

        public RawGetTransactions(InputKey privateKey, AccountAddress accountAddress, InternalTransactionId fromTransactionId) {
            this.privateKey = privateKey;
            this.accountAddress = accountAddress;
            this.fromTransactionId = fromTransactionId;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link RawTransactions RawTransactions} </p>
         */
        public RawGetTransactions() {
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
     *
     * <p> Returns {@link Ok Ok} </p>
     */
    public static class RawSendMessage extends Function {
        public byte[] body;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1789427488;

        public RawSendMessage(byte[] body) {
            this.body = body;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public RawSendMessage() {
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
        public String dir;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2039925427;

        public RunTests(String dir) {
            this.dir = dir;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Ok Ok} </p>
         */
        public RunTests() {
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
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -1364199535;

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
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2095589738;

        /**
         * Creates a function, which sets the verbosity level for a specified tonlib internal log tag. This is an offline method. Can be called before authorization. Can be called synchronously.
         *
         * <p> Returns {@link Ok Ok} </p>
         *
         * @param tag Logging tag to change verbosity level.
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
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -303429678;

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
        public long id;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -2115626088;

        public SmcGetCode(long id) {
            this.id = id;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link TvmCell TvmCell} </p>
         */
        public SmcGetCode() {
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
        public long id;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -427601079;

        public SmcGetData(long id) {
            this.id = id;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link TvmCell TvmCell} </p>
         */
        public SmcGetData() {
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
        public long id;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -214390293;

        public SmcGetState(long id) {
            this.id = id;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link TvmCell TvmCell} </p>
         */
        public SmcGetState() {
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
        public AccountAddress accountAddress;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -903491521;

        public SmcLoad(AccountAddress accountAddress) {
            this.accountAddress = accountAddress;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link SmcInfo SmcInfo} </p>
         */
        public SmcLoad() {
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
        public long id;
        public SmcMethodId method;
        public TvmStackEntry[] stack;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -255261270;

        public SmcRunGetMethod(long id, SmcMethodId method, TvmStackEntry[] stack) {
            this.id = id;
            this.method = method;
            this.stack = stack;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link SmcRunResult SmcRunResult} </p>
         */
        public SmcRunGetMethod() {
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
     *
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
        public String accountAddress;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -682459063;

        public UnpackAccountAddress(String accountAddress) {
            this.accountAddress = accountAddress;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link UnpackedAccountAddress UnpackedAccountAddress} </p>
         */
        public UnpackAccountAddress() {
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
        public TonBlockIdExt id;
        public Function function;

        /**
         * Identifier uniquely determining type of the object.
         */
        public static final int CONSTRUCTOR = -789093723;

        public WithBlock(TonBlockIdExt id, Function function) {
            this.id = id;
            this.function = function;
        }

        /**
         * Default constructor for a function, which
         *
         * <p> Returns {@link Object Object} </p>
         */
        public WithBlock() {
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
