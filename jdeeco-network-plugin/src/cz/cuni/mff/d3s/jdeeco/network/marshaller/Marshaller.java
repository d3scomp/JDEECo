package cz.cuni.mff.d3s.jdeeco.network.marshaller;

/**
 * Interface for data marshalers. These are responsible for encoding/decoding objects/binary data.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public interface Marshaller {
	/**
	 * Encodes object into binary format
	 * 
	 * @param data
	 *            Object to be encoded
	 * @return Binary data describing the object
	 * @throws Exception
	 */
	public byte[] marshall(Object data) throws Exception;

	/**
	 * Decodes binary data into object
	 * 
	 * @param data
	 *            Binary data to be decoded
	 * @return decoded object
	 * @throws Exception
	 */
	public Object unmashall(byte[] data) throws Exception;
}
