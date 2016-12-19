using System;
using System.Collections.Generic;
using System.Text;
using Windows.Security.Cryptography;
using Windows.Security.Cryptography.Core;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Storage.Streams;

namespace DWSS.Encryption
{
    public class Encrypter
    {
        private static string localPrivateKey = "<RSAKeyValue><Modulus>refs3E+0rNIPBcDHOX67ybmoHXWOYTOl9rgb1O2JF0X8jQFS9Wskaakk6KW8ePqVEExlPlaLY7HQ+bTdx8WThrDKVw8N6cvl9IBQ3GJyvkr6raHxGMKuq1T6SI7Mke681YI532g9Jtg8Oj5hDu67MDRT1u/YNs6ZIJgTxIVRCfM=</Modulus><Exponent>AQAB</Exponent><P>uiZKrdamE9iFD7zgaBNxv+QNDPcMvyWct9B14+z2tleP4HVv2tUPZqnapOxaJpof3egfgZD9cMiq7qFfaPoPWQ==</P><Q>7yl/6H+DG4n6O7jiZQ+2ge+IdmanG87/FBER8aGzMjDL6kp2RCbg85RNBqBmJCorJOuYMWBZOR4HnO4or2tmKw==</Q><DP>P1LDWLuKJ3oiWaYw2Ha03UggwGZSKbwLwePiFrUHgCPtR7frYw/Te2MQg8X7LOKFMKHEBflinblFnIO8xyM5kQ==</DP><DQ>n5yYqrf+7fAlupnnLf9QlGMiNbLIr/xzrBbAXPzHtSVv0cskJtOzqZw908BUqqoh5R/+kGV25nGuLwOaz4fjxQ==</DQ><InverseQ>ntT8e8y9T7QXZHAyJhE6aPPxUFFrg5B5rhxm9wGPHTbH7/MGgma62qrfsFxlpGcscpngev9Hsx7oNCsCLYqpvQ==</InverseQ><D>ADLQiKU/8VoPJnQEr1aI8quJ8OYiubRfBwT5mgtufwDpgqtBVpXniirtqJWf1WsmhLghQz4v1l6s9I8JW/+Pxbr6K6Hdh/zPDIurLk1gQo/f/VbPrOLuRI2DJlaZAzJlM+SVX9U84Gj/RQDpe0CChUafZ6xKBqqdMwMMvkBToqk=</D></RSAKeyValue>";
        //private static string localPublicKey = "<RSAKeyValue><Modulus>refs3E+0rNIPBcDHOX67ybmoHXWOYTOl9rgb1O2JF0X8jQFS9Wskaakk6KW8ePqVEExlPlaLY7HQ+bTdx8WThrDKVw8N6cvl9IBQ3GJyvkr6raHxGMKuq1T6SI7Mke681YI532g9Jtg8Oj5hDu67MDRT1u/YNs6ZIJgTxIVRCfM=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
        private static string restPublicKey = "<RSAKeyValue><Modulus>xfJ2lE3tFFlj58Snn+sPeJ+6m2J4os8+IboC2DaOzy+A68XaiTs3jYu4lwNP5oGN9xpyahj4e8qr3DiLikPFP0Z2d1QvkV+xQ5l55Ocd8Z1LX9eF2DSS19fO9sB1feUom9E1Kr+NYILI+lFo3TdHtAVMN+X4Hn13JfcVlX5KU7s=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";

        private static string privateBlob = "BwIAAACkAABSU0EyAAQAAAEAAQDZ2eq7SwNU7CQgDyjCkkBrkOeBH04SLEXY+vscbrftDyR8hWxAQiBjbKrTfpJmK3oURfdeia8b0Wecn+mjZWOTL74a8j/CHpmDo/FIxnfPHEQFMdT+sm/96Q8BLpvyZu0/bew6j2s6XmAtdiVX3/jL+wusTRxeBMbihc8j6yPTwI2vd1teA3Kyc5JfkdE2Ir9/WGQMRUEo9JN4n0Id1wAbjyDGZUiwDooCWT+4ZyYDw8gGUxLGWQHAjUwBfpLpneV9KqEolNySm3X6KdUMnulljll9zKPaE72rqJpsGq2Zwirh5q810pdS0mIB3Z0hWXVS4K9rpiOznEMlUVbvAPvWXa1vbvqMb5fqzAwmBRYPc77hDhuSAN4MP2eHqr0DAhtY3D/hxqqiWA/ucNIQndm8HHxwb8Jq3Qg6tod8N+JHd9FoHkYqS1pszQFNJkU8YX1Jug5ao0xZSSTpli5/ddS+uE6xNyQMbx0sxlBtpsQfBVKvZyvaRYKFSFSs2CFWyFvUJ1agi96RRNPdJPHcyNoMwGOuQqfLyuLzuXXiOpaoZ4QrdASKDCkUE5bkoMb87ny2m5qXuGT8cYkqlK32LCc/8R9UcSOQRmsF3IB+p+eK0JL2Rrd5c6kdkZ1RusBUn5UZ2GmMwZOQJsCWH7qm4SxQWVpZunQyQisPoSbO8vk5qb8WFgxptCsOIDnWRsDTs6g+7JSU8/Z0Ec03KIc381Y8Er2PUAjFqxUsu3kjUmhouvg1I1z78WdkKpWLlBQWhAM=";
        private static string publicBlob = "BgIAAACkAABSU0ExAAQAAAEAAQDZ2eq7SwNU7CQgDyjCkkBrkOeBH04SLEXY+vscbrftDyR8hWxAQiBjbKrTfpJmK3oURfdeia8b0Wecn+mjZWOTL74a8j/CHpmDo/FIxnfPHEQFMdT+sm/96Q8BLpvyZu0/bew6j2s6XmAtdiVX3/jL+wusTRxeBMbihc8j6yPTwA==";

        public static string Encrypt(string clearText)
        {
            ////IBuffer keyBuffer = CryptographicBuffer.DecodeFromBase64String(restPublicKey);
            //AsymmetricKeyAlgorithmProvider asym = AsymmetricKeyAlgorithmProvider.OpenAlgorithm(AsymmetricAlgorithmNames.RsaPkcs1);
            //CryptographicKey key = asym.ImportPublicKey(CryptographicBuffer.DecodeFromBase64String(restPublicKey), CryptographicPublicKeyBlobType.BCryptPublicKey);
            //IBuffer plainBuffer = CryptographicBuffer.ConvertStringToBinary(clearText, BinaryStringEncoding.Utf8);
            //IBuffer encryptedBuffer = CryptographicEngine.Encrypt(key, plainBuffer, null);
            //byte[] encryptedBytes;
            //CryptographicBuffer.CopyToByteArray(encryptedBuffer, out encryptedBytes);
            //return Convert.ToBase64String(encryptedBytes);

            string cspBlobString = publicBlob;
            var keyBlob = CryptographicBuffer.DecodeFromBase64String(cspBlobString);

            AsymmetricKeyAlgorithmProvider rsa = AsymmetricKeyAlgorithmProvider.OpenAlgorithm(AsymmetricAlgorithmNames.RsaPkcs1);
            CryptographicKey key = rsa.ImportPublicKey(keyBlob, CryptographicPublicKeyBlobType.Capi1PublicKey);

            IBuffer plainBuffer = CryptographicBuffer.ConvertStringToBinary(clearText, BinaryStringEncoding.Utf8);
            IBuffer encryptedBuffer = CryptographicEngine.Encrypt(key, plainBuffer, null);

            byte[] encryptedBytes;
            CryptographicBuffer.CopyToByteArray(encryptedBuffer, out encryptedBytes);

            return Convert.ToBase64String(encryptedBytes);
        }

        public static string Decrypt(string encryptedText)
        {
            byte[] encryptedTextBytes = Convert.FromBase64String(encryptedText);
            IBuffer keyBuffer = CryptographicBuffer.DecodeFromBase64String(localPrivateKey);
            AsymmetricKeyAlgorithmProvider asym = AsymmetricKeyAlgorithmProvider.OpenAlgorithm(AsymmetricAlgorithmNames.RsaPkcs1);
            CryptographicKey key = asym.ImportKeyPair(keyBuffer, CryptographicPrivateKeyBlobType.Capi1PrivateKey);
            IBuffer plainBuffer = CryptographicEngine.Decrypt(key, encryptedTextBytes.AsBuffer(), null);
            byte[] plainBytes;
            CryptographicBuffer.CopyToByteArray(plainBuffer, out plainBytes);
            return Encoding.UTF8.GetString(plainBytes, 0, plainBytes.Length);
        }
    }
}
