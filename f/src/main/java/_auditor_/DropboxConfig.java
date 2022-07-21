package com.wsudesc.app;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.IncludeGrantedScopes;
import com.dropbox.core.TokenAccessType;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;

public class DropboxConfig {

  private String dropboxAppSecret;
  private String dropboxAppKey;
  private String dropboxRefreshToken;
  private String dropboxAccessToken;
  private String dropboxFileConf;

  public DropboxConfig (String fileConf) {

    try (InputStream input = new FileInputStream(fileConf)) {

      Properties prop = new Properties();
      prop.load(input);
      dropboxAccessToken = prop.getProperty("dropbox.access.token");
      dropboxRefreshToken = prop.getProperty("dropbox.refresh.token");
      dropboxAppKey = prop.getProperty("dropbox.app.key");
      dropboxAppSecret = prop.getProperty("dropbox.app.secret");
      dropboxFileConf = fileConf;

    } catch (IOException ex) {
      ex.printStackTrace();
    }

  }

  public DbxClientV2 DropboxClient() {

    DbxRequestConfig config = DbxRequestConfig.newBuilder(dropboxFileConf).build();
    DbxCredential credentials = new DbxCredential(dropboxAccessToken, -1L, dropboxRefreshToken, dropboxAppKey, dropboxAppSecret);

    return new DbxClientV2(config, credentials);
  }

}
