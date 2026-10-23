package com.ferbo.gestion.api.tool;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.zip.ZipOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IOUtil 
{
    private static Logger log = LogManager.getLogger(IOUtil.class);

    public static synchronized void close(ZipOutputStream output) 
    {
        try {
            if (output != null) {
                output.finish();
                output.close();
            }
        } catch (Exception ex) {
            log.error("Error al crear o comprimir un archivo comprimido: {}", ex);
        } finally {
            output = null;
        }
    }

    public static synchronized void close(OutputStream output) {

        try {
            if (output != null) {
                output.close();
            }
        } catch (Exception ex) {
            log.error("Ocurrio un error: {}", ex);
        } finally {
            output = null;
        }
    }

    public static synchronized void close(Writer writer) {
        try {
            if (writer != null) {
                writer.flush();
            }
        } catch (Exception ex) {
            log.error("Ocurrio un error: {}", ex);
        }

        try {
            if (writer != null) {
                writer.close();
            }
        } catch (Exception ex) {
            log.error("Ocurrio un error: {}", ex);
        } finally {
            writer = null;
        }
    }

    public static synchronized void close(InputStream input) {

        try {
            if (input != null) {
                input.close();
            }
        } catch (Exception ex) {
            log.error("Ocurrio un error: {}", ex);
        } finally {
            input = null;
        }
    }

    public static void close(BufferedReader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (Exception ex) {
            log.error("Ocurrio un error: {}", ex);
        } finally {
            reader = null;
        }

    }

    public static byte[] read(InputStream input) throws IOException {
        byte[] resultado = null;
        byte[] buffer = new byte[1024];

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int nRead;

        while ((nRead = input.read(buffer, 0, buffer.length)) != -1) {
            output.write(buffer, 0, nRead);
        }

        resultado = output.toByteArray();
        return resultado;
    }   

}
