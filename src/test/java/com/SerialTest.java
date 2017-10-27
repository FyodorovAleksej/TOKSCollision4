package com;
import jssc.SerialPort;
import jssc.SerialPortList;
import junit.framework.TestCase;

public class SerialTest extends TestCase {
    static Serial serialPort1 = null;
    static Serial serialPort2 = null;
    static boolean start;

    public void setUp() throws Exception {
        String[] names = SerialPortList.getPortNames();
        if (names.length >= 2) {
            start = true;
            serialPort1 = new Serial(names[0]);
            serialPort2 = new Serial(names[1]);
        }
        else {
            start = false;
        }

        if (start) {
            if (!serialPort1.isOpen()) {
                serialPort1.open();
            }
            if (!serialPort2.isOpen()) {
                serialPort2.open();
            }
            serialPort1.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            serialPort1.setFlowControl(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);

            serialPort2.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            serialPort2.setFlowControl(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
        }
        super.setUp();
    }

    public void tearDown() throws Exception {
        //serialPort1.close();
        //serialPort2.close();
    }

    public void testStart() throws Exception {
        assertTrue(start);
    }
/*
    public void testWrite() throws Exception {
        if (start) {
            String test = "Test String\nTest String2";
            serialPort1.write(test.getBytes(), "COM2", "COM1");
            assertEquals(new String(serialPort2.read(test.length())), test);
        }
    }

    public void testRead() throws Exception {
        if (start) {
            String test = "Test String\nTest String2";
            serialPort1.write(test.getBytes(), "COM2", "COM2");
            serialPort1.write(test.getBytes(), "COM2", "COM1");
            assertEquals(new String(serialPort2.read(2 * test.length())), test + test);
        }
    }

    public void testClose() throws Exception {
        if (start) {
            assertEquals(serialPort1.close(), true);
            assertEquals(serialPort1.close(), false);
        }
    }

    public void testIsOpen() throws Exception {
        if (start) {
            assertEquals(serialPort1.isOpen(), true);
            serialPort1.close();
            assertEquals(serialPort1.isOpen(), false);
        }
    }
    */

    public void testHeming() throws Exception {
        //"xrja"
        System.out.println(Serial.toBinary("xr".getBytes()));
        System.out.println(Serial.toBinary("ja".getBytes()));
        for (int i = 0; i < 19; i++) {
            assertEquals(new String(serialPort1.hemingDecode(serialPort1.hemingCoding("xr".getBytes(), i))),"xr");
        }
        for (int i = 0; i < 19; i++) {
            assertEquals(new String(serialPort1.hemingDecode(serialPort1.hemingCoding("ja".getBytes(), i))),"ja");
        }
    }

    public void testChar() throws Exception {
        int a  = (int) '1'- '0';
        System.out.println(a);
    }

    public void testdivideComplex() throws Exception {
        testDivide1();
        testDivide2();
        testDivide3();
    }

    public void testDivide1() throws Exception {
        assertEquals(Serial.divide("1010","1101"),"0111");
    }

    public void testDivide2() throws Exception {
        assertEquals(Serial.divide("1011","1101"),"0110");
    }

    public void testDivide3() throws Exception {
        assertEquals(Serial.divide("0110","1101"),null);
    }

    public void testShiftLeft() throws Exception {
        assertEquals(Serial.shiftLeft("1011"),"0111");
    }
    public void testShiftRight() throws Exception {
        assertEquals(Serial.shiftRight("0110"), "0011");
    }

    public void testGetMod() throws Exception {
        assertEquals(Serial.getMod("11010101101","1101"),"011");
    }

    public void testRepair() throws Exception {
        assertEquals(Serial.repair("1011001"),"1010001");
    }

    public void testRepair1() throws Exception {
        assertEquals(Serial.repair("1110001"),"1010001");
    }

    public void testGetMod1() throws Exception {
        assertEquals(Serial.getMod("1010000","1101"),"001");
    }

    public void testFullRepair() throws Exception {
        String raw = "1010000";
        assertEquals(Serial.repair("1110" + Serial.getMod(raw,"1101")),"1010" + Serial.getMod(raw,"1101"));
    }

    public void testCRC() throws Exception {
        String s = "f";
        assertEquals(new String(Serial.crcDecoding(Serial.crcCoding(s.getBytes()))).substring(0,1),s);
    }

    public void testCRCVar() throws Exception {
        String s = "a";
        System.out.println(Serial.toBinary(s.getBytes()));
        System.out.println(Serial.toBinary(s.getBytes()).substring(4));
        String raw = Serial.toBinary(s.getBytes()).substring(4);
        System.out.println(raw + Serial.getMod(raw + "000", "1101"));
        String eror = raw.replaceFirst("0","1");
        System.out.println(eror);
        assertEquals(Serial.toBinary(Serial.crcDecoding(Serial.fromBinary(eror + Serial.getMod(raw + "000","1101")))).substring(0,4),raw);
        //assertEquals(new String(Serial.crcDecoding(Serial.crcCoding(s.getBytes()))),s);
    }
}