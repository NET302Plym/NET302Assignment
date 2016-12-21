using System;
using System.Collections.Generic;
using System.Text;
using Windows.Security.Cryptography;
using Windows.Security.Cryptography.Core;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Storage.Streams;
using System.IO;
using System.Threading.Tasks;

namespace DWSS.Encryption
{
    public class Encrypter
    {
        private string keyString = "NET_302_Plym_KEY";
        private string ivString = "YEK_mylP_203_TEN";
        
        BinaryStringEncoding encoding = BinaryStringEncoding.Utf8;
        IBuffer iv;
        CryptographicKey key;
        string strAlgName = SymmetricAlgorithmNames.AesCbcPkcs7;
        UInt32 keyLength = 128;
        SymmetricKeyAlgorithmProvider objAlg;
        
        public Encrypter()
        {
            objAlg = SymmetricKeyAlgorithmProvider.OpenAlgorithm(strAlgName);
            //while (keyString.Length < 16)
            //    keyString += "=";
            //while (ivString.Length < 16)
            //    ivString += "=";
            if (strAlgName.Contains("CBC"))
                iv = CryptographicBuffer.ConvertStringToBinary(ivString, encoding);
            IBuffer keyMaterial = CryptographicBuffer.ConvertStringToBinary(keyString, encoding);
            key = objAlg.CreateSymmetricKey(keyMaterial);
            
        }

        public string EncryptString(string s)
        {
            return CryptographicBuffer.EncodeToBase64String(CipherEncryption(s));
        }

        public string DecryptString(string s)
        {
            return CipherDecryption(CryptographicBuffer.DecodeFromBase64String(s));
        }

        private IBuffer CipherEncryption(string strMsg)
        {
            string localStrMsg = strMsg;
            IBuffer buffMsg = CryptographicBuffer.ConvertStringToBinary(localStrMsg, encoding);
            if (!strAlgName.Contains("PKCS7"))
            {
                while (buffMsg.Length % objAlg.BlockLength != 0)
                {
                    localStrMsg += "=";
                    buffMsg = CryptographicBuffer.ConvertStringToBinary(localStrMsg, encoding);
                }
            }
            return CryptographicEngine.Encrypt(key, buffMsg, iv);
        }

        private string CipherDecryption(IBuffer buffEncrypt)
        {
            IBuffer buffDecrypted;
            buffDecrypted = CryptographicEngine.Decrypt(key, buffEncrypt, iv);
            return CryptographicBuffer.ConvertBinaryToString(encoding, buffDecrypted);
        }
    }
}