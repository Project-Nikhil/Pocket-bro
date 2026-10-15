import { useState } from 'react';
import { ExternalLink, Smartphone, MessageSquare, Calendar, Home, Wrench, Sparkles, CheckCircle2 } from 'lucide-react';
import { APP_CONFIG } from '../config/appConfig';

interface HeroSectionProps {
  onOpenBuildGuide?: () => void;
}

export const HeroSection = ({ onOpenBuildGuide }: HeroSectionProps) => {
  const [activeTab, setActiveTab] = useState<'chat' | 'home' | 'calendar' | 'tools' | 'onboarding'>('chat');

  const previews = {
    chat: {
      image: './assets/mockup-chat.png',
      alt: 'Pocket Brother WhatsApp-Style Chat with 3 Personality Modes',
      label: 'Brother Chat',
      tag: 'WhatsApp-style chat with Comfort, Irritative, & Funny modes',
    },
    home: {
      image: './assets/mockup-home.png',
      alt: 'Pocket Brother Home Hub with Live Clock and Night Sky',
      label: 'Home Hub',
      tag: 'Daily warm quote, live night sky card, & all quick tools',
    },
    calendar: {
      image: './assets/mockup-calendar.png',
      alt: 'Pocket Brother Calendar and Schedule Planner',
      label: 'Calendar',
      tag: 'Event schedules, exam dates, & talk-to-brother slots',
    },
    tools: {
      image: './assets/mockup-tools.png',
      alt: 'Pocket Brother Timer, Reminders, and Notes',
      label: 'Daily Tools',
      tag: 'Study Pomodoro timer, reminders, and diary notes',
    },
    onboarding: {
      image: './assets/mockup-onboarding.png',
      alt: 'Pocket Brother Relationship Selection Onboarding',
      label: 'Onboarding',
      tag: 'Choose Elder Brother or Younger Brother relationship',
    },
  };

  const currentPreview = previews[activeTab];

  return (
    <section style={{
      position: 'relative',
      paddingTop: '24px',
      paddingBottom: '48px',
      overflow: 'hidden',
    }}>
      {/* Ambient background glow */}
      <div style={{
        position: 'absolute',
        top: 0,
        left: '50%',
        transform: 'translateX(-50%)',
        width: '100%',
        maxWidth: '600px',
        height: '350px',
        background: 'radial-gradient(ellipse at top, rgba(245, 197, 138, 0.12) 0%, transparent 70%)',
        pointerEvents: 'none',
        zIndex: 0,
      }} />

      <div className="container" style={{ position: 'relative', zIndex: 1 }}>
        <div className="hero-grid">
          
          {/* Main Info Column */}
          <div className="hero-content">
            
            {/* Mobile App Header Badge */}
            <div style={{
              display: 'flex',
              alignItems: 'center',
              gap: '12px',
              marginBottom: '16px',
            }}>
              <div style={{
                width: '46px',
                height: '46px',
                borderRadius: '14px',
                background: 'linear-gradient(135deg, #2E231E 0%, #1A1412 100%)',
                border: '1px solid var(--border-accent)',
                overflow: 'hidden',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                boxShadow: '0 4px 14px rgba(0,0,0,0.4)',
                flexShrink: 0,
              }}>
                <img
                  src="./assets/brother-cat-hero.png"
                  alt="Pocket Brother Avatar"
                  style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                />
              </div>

              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                  <span style={{
                    fontSize: '0.8rem',
                    fontWeight: 700,
                    color: 'var(--accent-amber)',
                    textTransform: 'uppercase',
                    letterSpacing: '0.06em',
                  }}>
                    Pocket Brother
                  </span>
                  <span style={{ color: '#F87171', fontSize: '0.85rem' }}>♡</span>
                </div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                  Android Companion App • v{APP_CONFIG.version}
                </div>
              </div>
            </div>

            {/* Headline */}
            <h1 className="hero-headline">
              “a little piece of home, whenever you need me ♡”
            </h1>

            {/* Sub-headline */}
            <p className="hero-subheadline">
              {APP_CONFIG.headline}
            </p>

            {/* Description */}
            <p className="hero-description">
              {APP_CONFIG.description}
            </p>

            {/* Mobile Quick Spec Chips */}
            <div className="mobile-spec-chips">
              <span className="spec-chip">
                <Smartphone size={13} color="var(--accent-amber)" />
                <span>Android 7.0+</span>
              </span>
              <span className="spec-chip">
                <span>📦 {APP_CONFIG.apkEstimatedSize}</span>
              </span>
              <span className="spec-chip">
                <span>⚡ 100% Offline Core</span>
              </span>
              <span className="spec-chip">
                <span>🔒 No Sign-up</span>
              </span>
            </div>

            {/* Main Action Buttons */}
            <div className="hero-cta-container">
              <a
                href={APP_CONFIG.apkDownloadPath}
                download={APP_CONFIG.apkFileName}
                className="btn-primary hero-btn-main"
                id="hero-download-btn"
              >
                <Smartphone size={20} />
                <span style={{ fontWeight: 700 }}>Download for Android</span>
                <span className="cta-badge">
                  {APP_CONFIG.apkEstimatedSize}
                </span>
              </a>

              <a
                href="#features"
                className="btn-secondary hero-btn-secondary"
              >
                Explore Features
              </a>
            </div>

            {/* Sideload Security & Fallback Links */}
            <div className="hero-footnote">
              <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                <CheckCircle2 size={14} color="#34D399" />
                <span>Direct APK installer (No Play Store required)</span>
              </div>
              <div>
                <span>Trouble downloading? </span>
                <a
                  href={APP_CONFIG.apkDownloadPath}
                  target="_blank"
                  rel="noopener noreferrer"
                  style={{
                    color: 'var(--accent-amber)',
                    textDecoration: 'underline',
                    display: 'inline-flex',
                    alignItems: 'center',
                    gap: '3px',
                  }}
                >
                  Direct APK Link
                  <ExternalLink size={12} />
                </a>
                {onOpenBuildGuide && (
                  <>
                    <span> • </span>
                    <button
                      onClick={onOpenBuildGuide}
                      style={{
                        color: 'var(--text-muted)',
                        textDecoration: 'underline',
                        fontSize: 'inherit',
                      }}
                    >
                      Build guide
                    </button>
                  </>
                )}
              </div>
            </div>
          </div>

          {/* Interactive Screen Preview Container */}
          <div className="hero-preview-container">
            {/* Scrollable pill tab selector for mobile thumbs */}
            <div className="preview-tab-bar no-scrollbar">
              <button
                onClick={() => setActiveTab('chat')}
                className={`preview-tab-btn ${activeTab === 'chat' ? 'active' : ''}`}
              >
                <MessageSquare size={13} />
                <span>Chat</span>
              </button>

              <button
                onClick={() => setActiveTab('home')}
                className={`preview-tab-btn ${activeTab === 'home' ? 'active' : ''}`}
              >
                <Home size={13} />
                <span>Home Hub</span>
              </button>

              <button
                onClick={() => setActiveTab('calendar')}
                className={`preview-tab-btn ${activeTab === 'calendar' ? 'active' : ''}`}
              >
                <Calendar size={13} />
                <span>Calendar</span>
              </button>

              <button
                onClick={() => setActiveTab('tools')}
                className={`preview-tab-btn ${activeTab === 'tools' ? 'active' : ''}`}
              >
                <Wrench size={13} />
                <span>Tools</span>
              </button>

              <button
                onClick={() => setActiveTab('onboarding')}
                className={`preview-tab-btn ${activeTab === 'onboarding' ? 'active' : ''}`}
              >
                <Sparkles size={13} />
                <span>Onboarding</span>
              </button>
            </div>

            {/* Phone Screen Viewport Card */}
            <div className="mockup-frame">
              <div className="mockup-inner">
                <img
                  src={currentPreview.image}
                  alt={currentPreview.alt}
                  className="mockup-image"
                />
              </div>

              {/* Status pill notch on desktop only */}
              <div className="mockup-notch" />
            </div>

            {/* Screen descriptor */}
            <p className="mockup-caption">
              <strong>{currentPreview.label}:</strong> {currentPreview.tag}
            </p>
          </div>

        </div>
      </div>

      <style>{`
        .hero-grid {
          display: flex;
          flex-direction: column;
          gap: 32px;
        }

        .hero-headline {
          font-size: 1.85rem;
          line-height: 1.2;
          font-weight: 700;
          color: var(--text-cream);
          margin-bottom: 12px;
        }

        .hero-subheadline {
          font-size: 1.1rem;
          color: var(--accent-peach);
          font-weight: 600;
          margin-bottom: 8px;
        }

        .hero-description {
          font-size: 0.95rem;
          color: var(--text-muted);
          line-height: 1.55;
          margin-bottom: 20px;
        }

        .mobile-spec-chips {
          display: flex;
          flex-wrap: wrap;
          gap: 8px;
          margin-bottom: 22px;
        }

        .spec-chip {
          display: inline-flex;
          align-items: center;
          gap: 5px;
          padding: 4px 10px;
          background: rgba(36, 29, 25, 0.7);
          border: 1px solid var(--border-medium);
          border-radius: var(--radius-full);
          font-size: 0.775rem;
          color: var(--text-muted);
          font-weight: 500;
        }

        .hero-cta-container {
          display: flex;
          flex-direction: column;
          gap: 12px;
          margin-bottom: 18px;
        }

        .hero-btn-main {
          width: 100%;
          padding: 15px 20px;
          font-size: 1.025rem;
          justifyContent: center;
        }

        .hero-btn-secondary {
          width: 100%;
          justifyContent: center;
          padding: 13px 20px;
        }

        .cta-badge {
          font-size: 0.75rem;
          padding: 2px 8px;
          border-radius: 10px;
          background: rgba(26, 18, 11, 0.25);
          font-weight: 700;
          margin-left: 4px;
        }

        .hero-footnote {
          display: flex;
          flex-direction: column;
          gap: 6px;
          font-size: 0.825rem;
          color: var(--text-subtle);
        }

        .hero-preview-container {
          display: flex;
          flex-direction: column;
          align-items: center;
          width: 100%;
        }

        .preview-tab-bar {
          display: flex;
          align-items: center;
          gap: 6px;
          padding: 4px;
          background: var(--surface-card);
          border: 1px solid var(--border-medium);
          border-radius: var(--radius-full);
          margin-bottom: 14px;
          max-width: 100%;
          overflow-x: auto;
          -webkit-overflow-scrolling: touch;
        }

        .preview-tab-btn {
          display: flex;
          align-items: center;
          gap: 5px;
          padding: 7px 13px;
          border-radius: var(--radius-full);
          font-size: 0.8rem;
          font-weight: 600;
          color: var(--text-muted);
          white-space: nowrap;
          transition: all 0.2s;
          background: transparent;
        }

        .preview-tab-btn.active {
          background: var(--accent-amber);
          color: #1A120B;
        }

        .mockup-frame {
          position: relative;
          width: 100%;
          max-width: 320px;
          border-radius: 36px;
          padding: 8px;
          background: linear-gradient(145deg, #3A2F27 0%, #1A1411 50%, #2D231D 100%);
          box-shadow: 0 16px 45px rgba(0, 0, 0, 0.6), 0 0 30px rgba(245, 197, 138, 0.08);
          border: 1.5px solid rgba(245, 197, 138, 0.22);
        }

        .mockup-inner {
          position: relative;
          border-radius: 28px;
          overflow: hidden;
          background-color: #14110F;
          border: 1px solid #2B211C;
          aspect-ratio: 9 / 19.5;
        }

        .mockup-image {
          width: 100%;
          height: 100%;
          object-fit: cover;
          display: block;
        }

        .mockup-notch {
          position: absolute;
          top: 16px;
          left: 50%;
          transform: translateX(-50%);
          width: 68px;
          height: 4px;
          border-radius: 2px;
          background: rgba(0,0,0,0.85);
          z-index: 10;
        }

        .mockup-caption {
          margin-top: 12px;
          font-size: 0.825rem;
          color: var(--text-muted);
          text-align: center;
          max-width: 300px;
        }

        .mockup-caption strong {
          color: var(--accent-amber);
        }

        /* Desktop Breakpoints */
        @media (min-width: 640px) {
          .hero-cta-container {
            flex-direction: row;
          }
          .hero-btn-main {
            width: auto;
          }
          .hero-btn-secondary {
            width: auto;
          }
        }

        @media (min-width: 960px) {
          .hero-grid {
            display: grid;
            grid-template-columns: 1.15fr 0.85fr;
            gap: 50px;
            align-items: center;
          }
          .hero-headline {
            font-size: 3rem;
            line-height: 1.15;
          }
          .hero-subheadline {
            font-size: 1.25rem;
          }
          .mockup-frame {
            max-width: 340px;
            padding: 10px;
            border-radius: 42px;
          }
          .mockup-inner {
            border-radius: 34px;
          }
        }
      `}</style>
    </section>
  );
};
