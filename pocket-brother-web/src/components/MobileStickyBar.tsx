import { useState, useEffect } from 'react';
import { Download, Heart } from 'lucide-react';
import { APP_CONFIG } from '../config/appConfig';

export const MobileStickyBar = () => {
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      // Show sticky bar once user scrolls past top 200px
      if (window.scrollY > 200) {
        setIsVisible(true);
      } else {
        setIsVisible(false);
      }
    };

    window.addEventListener('scroll', handleScroll, { passive: true });
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  if (!isVisible) return null;

  return (
    <div
      className="mobile-sticky-bar"
      style={{
        position: 'fixed',
        bottom: 0,
        left: 0,
        right: 0,
        zIndex: 90,
        backgroundColor: 'rgba(22, 17, 14, 0.94)',
        backdropFilter: 'blur(16px)',
        borderTop: '1px solid var(--border-medium)',
        padding: '10px 16px',
        paddingBottom: 'max(12px, env(safe-area-inset-bottom))',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        gap: '12px',
        boxShadow: '0 -4px 20px rgba(0,0,0,0.5)',
        animation: 'slideUp 0.25s cubic-bezier(0.16, 1, 0.3, 1)',
      }}
    >
      {/* Left: App icon & info */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '10px', minWidth: 0 }}>
        <div style={{
          width: '38px',
          height: '38px',
          borderRadius: '10px',
          background: 'linear-gradient(135deg, #2E231E 0%, #1A1412 100%)',
          border: '1px solid var(--border-accent)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          flexShrink: 0,
        }}>
          <Heart size={18} fill="#F87171" color="#F87171" />
        </div>
        <div style={{ minWidth: 0 }}>
          <div style={{
            fontSize: '0.9rem',
            fontWeight: 700,
            color: 'var(--text-cream)',
            whiteSpace: 'nowrap',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
          }}>
            Pocket Brother
          </div>
          <div style={{ fontSize: '0.725rem', color: 'var(--text-muted)' }}>
            v{APP_CONFIG.version} • {APP_CONFIG.apkEstimatedSize}
          </div>
        </div>
      </div>

      {/* Right: Primary Download Button */}
      <a
        href={APP_CONFIG.apkDownloadPath}
        download={APP_CONFIG.apkFileName}
        className="btn-primary"
        style={{
          padding: '10px 18px',
          fontSize: '0.9rem',
          flexShrink: 0,
          minHeight: '44px',
        }}
      >
        <Download size={16} />
        <span>Download APK</span>
      </a>

      <style>{`
        @keyframes slideUp {
          from { transform: translateY(100%); }
          to { transform: translateY(0); }
        }
        @media (min-width: 768px) {
          .mobile-sticky-bar {
            display: none !important;
          }
        }
      `}</style>
    </div>
  );
};
