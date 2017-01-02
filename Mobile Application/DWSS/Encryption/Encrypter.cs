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
    /// <summary>
    /// Provides methods for encrypting and decrypting strings from the server
    /// </summary>
    public class Encrypter
    {
        /// <summary>
        /// The Keystring used by the server
        /// </summary>
        private string keyString = "NET_302_Plym_KEY";
        /// <summary>
        /// The IV string used by the server 
        /// </summary>
        private string ivString = "YEK_mylP_203_TEN";
        /// <summary>
        /// UTF8 Encoding
        /// </summary>
        BinaryStringEncoding encoding = BinaryStringEncoding.Utf8;
        /// <summary>
        /// Holds the IV using the encoding 
        /// </summary>
        IBuffer iv;
        /// <summary>
        /// The key used in encryption / decryption
        /// </summary>
        CryptographicKey key;
        /// <summary>
        /// The type of algorithm to use 
        /// </summary>
        string strAlgName = SymmetricAlgorithmNames.AesCbcPkcs7;
        /// <summary>
        /// The length of key - possibly redundant 
        /// </summary>
        UInt32 keyLength = 128;
        /// <summary>
        /// A provider to use for the processing 
        /// </summary>
        SymmetricKeyAlgorithmProvider objAlg;
        /// <summary>
        /// Blank constructor on the outside, internally this sets all the required variables 
        /// </summary>
        public Encrypter()
        {
            objAlg = SymmetricKeyAlgorithmProvider.OpenAlgorithm(strAlgName);
            if (strAlgName.Contains("CBC"))
                iv = CryptographicBuffer.ConvertStringToBinary(ivString, encoding);
            IBuffer keyMaterial = CryptographicBuffer.ConvertStringToBinary(keyString, encoding);
            key = objAlg.CreateSymmetricKey(keyMaterial);
        }
        /// <summary>
        /// Encrypts a string to send to the server
        /// </summary>
        /// <param name="s"></param>
        /// <returns></returns>
        public string EncryptString(string s)
        {
            return CryptographicBuffer.EncodeToBase64String(CipherEncryption(s));
        }
        /// <summary>
        /// Decrypts a string recieved from the server 
        /// </summary>
        /// <param name="s"></param>
        /// <returns></returns>
        public string DecryptString(string s)
        {
            return CipherDecryption(CryptographicBuffer.DecodeFromBase64String(s));
        }
        /// <summary>
        /// Used internally to handle the encryption
        /// </summary>
        /// <param name="strMsg"></param>
        /// <returns></returns>
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
        /// <summary>
        /// Used internally to handle the decryption 
        /// </summary>
        /// <param name="buffEncrypt"></param>
        /// <returns></returns>
        private string CipherDecryption(IBuffer buffEncrypt)
        {
            IBuffer buffDecrypted;
            buffDecrypted = CryptographicEngine.Decrypt(key, buffEncrypt, iv);
            return CryptographicBuffer.ConvertBinaryToString(encoding, buffDecrypted);
        }
    }
}