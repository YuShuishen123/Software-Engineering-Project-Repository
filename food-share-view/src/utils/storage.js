const TOKEN_KEY = "token"
/**
 * 获取Token
 */
export function getToken() {
    return sessionStorage.getItem(TOKEN_KEY);
}
/**
 * 设置Token
 */
export function setToken(token) {
    sessionStorage.setItem(TOKEN_KEY, token);
}
/**
 * 清除Token
 */
export function clearToken() {
    sessionStorage.setItem(TOKEN_KEY, null);
}