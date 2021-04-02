<?php

const API_VERSION = 2;
const API_HOST = 'api.xroom.app';

class Xroom
{
    // Room types
    const ROOM_CONFERENCE   = 1;
    const ROOM_WEBINAR      = 2;

    /**
     * @var string
     */
    private string $secret;

    /**
     * @var string
     */
    private string $username;

    /**
     * Xroom constructor.
     *
     * @param string $username
     * @param string $secret
     */
    public function __construct(string $username, string $secret)
    {
        $this->secret = $secret;
        $this->username = $username;
    }

    /**
     * cURL API wrapper
     *
     * @param string $endpoint
     * @param string $method
     * @param null $payload
     * @return mixed
     * @throws \Exception
     */
    public function call(string $endpoint, string $method, $payload = null)
    {
        $random = generateSalt(32);
        $hmac = hash_hmac('sha256', $random, $this->secret);

        $payload = json_encode(['method' => $method, 'data' => $payload]);

        $ch = curl_init('https://' . API_HOST . '/api/' . $endpoint);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array
        (
            'Content-Type: application/json',
            'Content-Length: '  . strlen($payload),
            'x-api-version:'    . API_VERSION,
            'x-random:'         . $random,
            'x-auth-id:'        . $this->username,
            'x-auth-key:'       . $hmac,
        ));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $payload);

        $result = json_decode(curl_exec($ch));
        curl_close($ch);

        if (!$result || $result->isError)
        {
            throw new \Exception($result->errMsg);
        }

        return $result->data;
    }
}

/**
 * Just a helper function. Feel free to use your own.
 *
 * @param int $length
 * @param bool $specialSymbols
 * @return string
 */
function generateSalt(int $length = 8, bool $specialSymbols = true) : string
{
    $pass = '';
    $alphabet = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789' . ($specialSymbols ? '.,!#%=?+-' : '');

    for ($i = 0; $i < $length; $i++)
    {
        $n = rand(0, strlen($alphabet) - 1);
        $pass .= $alphabet[$n];
    }

    return $pass;
}
