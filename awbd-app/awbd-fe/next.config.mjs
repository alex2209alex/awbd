/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  typescript: {
    ignoreBuildErrors: true, // ✅ Ignores all TS errors
  },
  eslint: {
    ignoreDuringBuilds: true, // ✅ Ignores ESLint issues during build
  },
};

export default nextConfig;
