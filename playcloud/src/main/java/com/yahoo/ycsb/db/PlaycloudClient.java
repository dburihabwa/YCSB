package com.yahoo.ycsb.db;

import com.yahoo.ycsb.ByteIterator;
import com.yahoo.ycsb.DB;
import com.yahoo.ycsb.Status;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.*;

/**
 *
 */
public class PlaycloudClient extends DB {

  /**
   * @param table  The name of the table
   * @param key    The record key of the record to read.
   * @param fields The list of fields to read, or null for all of them
   * @param result A HashMap of field/value pairs for the result
   * @return
   */
  @Override
  public Status read(String table, String key, Set<String> fields, HashMap<String, ByteIterator> result) {
    Properties properties = this.getProperties();
    String host = properties.getProperty("playcloud.host");
    String port = properties.getProperty("playcloud.port");
    String url = "http://" + host + ":" + port + "/" + key;
    HttpGet request = new HttpGet(url);
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      CloseableHttpResponse response = client.execute(request);
      response.close();
    } catch (IOException e) {
      e.printStackTrace();
      return Status.ERROR;
    }
    return Status.OK;
  }

  /**
   * @param table       The name of the table
   * @param startkey    The record key of the first record to read.
   * @param recordcount The number of records to read
   * @param fields      The list of fields to read, or null for all of them
   * @param result      A Vector of HashMaps, where each HashMap is a set field/value pairs for one record
   * @return
   */
  @Override
  public Status scan(String table, String startkey, int recordcount, Set<String> fields,
                     Vector<HashMap<String, ByteIterator>> result) {
    return null;
  }

  /**
   * @param table  The name of the table
   * @param key    The record key of the record to write.
   * @param values A HashMap of field/value pairs to update in the record
   * @return
   */
  @Override
  public Status update(String table, String key, HashMap<String, ByteIterator> values) {
    return this.insert(table, key, values);
  }

  /**
   * @param table  The name of the table
   * @param key    The record key of the record to insert.
   * @param values A HashMap of field/value pairs to insert in the record
   * @return
   */
  @Override
  public Status insert(final String table, final String key, final HashMap<String, ByteIterator> values) {
    // Build URL
    Properties properties = this.getProperties();
    String host = properties.getProperty("playcloud.host");
    String port = properties.getProperty("playcloud.port");
    String url = "http://" + host + ":" + port + "/" + key;
    HttpPut request = new HttpPut(url);
    // Perpare payload
    byte[] payload = {};
    if (values.size() > 0) {
      TreeMap<String, ByteIterator> sortedValues = new TreeMap<String, ByteIterator>();
      sortedValues.putAll(values);
      Set<Map.Entry<String, ByteIterator>> entrySet = sortedValues.entrySet();
      Map.Entry<String, ByteIterator> firstEntry = entrySet.iterator().next();
      int bufferLength = (int) firstEntry.getValue().bytesLeft();
      int totalLength = values.size() * bufferLength;
      int offset = 0;
      payload = new byte[totalLength];
      for (Map.Entry<String, ByteIterator> entry: entrySet) {
        System.arraycopy(entry.getValue().toArray(), 0, payload, offset, bufferLength);
        offset += bufferLength;
      }
    }
    HttpEntity entity = new ByteArrayEntity(payload);
    request.setEntity(entity);
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      CloseableHttpResponse response = client.execute(request);
      response.close();
    } catch (IOException e) {
      e.printStackTrace();
      return Status.ERROR;
    }
    return Status.OK;
  }

  /**
   * @param table The name of the table
   * @param key   The record key of the record to delete.
   * @return
   */
  @Override
  public Status delete(String table, String key) {
    throw new UnsupportedOperationException("playcloud does not support DELETE operations");
  }
}
